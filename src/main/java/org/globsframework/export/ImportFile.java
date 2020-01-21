package org.globsframework.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.globsframework.export.annotation.ExportDateFormat;
import org.globsframework.export.annotation.ImportEmptyStringHasEmptyStringFormat;
import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.fields.*;
import org.globsframework.metamodel.utils.GlobTypeUtils;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;
import org.globsframework.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class ImportFile {
    private static Logger LOGGER = LoggerFactory.getLogger(ImportFile.class);
    private boolean withSeparator;
    private char separator;
    private Map<GlobType, ImportReader> importers = new HashMap<>();
    private ExportBySize.PaddingType paddingType;

    public ImportFile withSeparator(char separator) {
        withSeparator = true;
        this.separator = separator;
        importers = null;
        return this;
    }

    public ImportFile withRightPadding() {
        paddingType = ExportBySize.PaddingType.right;
        importers = null;
        return this;
    }

    public ImportFile withLeftPadding() {
        paddingType = ExportBySize.PaddingType.left;
        importers = null;
        return this;
    }

    public void importContent(InputStream inputStream, Consumer<Glob> consumer, GlobType globType) throws IOException {
        importContent(new InputStreamReader(new BOMInputStream(inputStream), "UTF-8"), consumer, globType);
    }

    public void importContent(Reader reader, Consumer<Glob> consumer, GlobType globType) throws IOException {
        if (withSeparator) {
            CSVParser parse = load(reader);
            DefaultDataRead dataRead = new DefaultDataRead(parse);

            dataRead.read(consumer, globType);

        } else {
            throw new RuntimeException("Not implemented");
        }
    }

    public DataRead getDataReader(InputStream inputStream) throws IOException {
        return new DefaultDataRead(load(new InputStreamReader(new BOMInputStream(inputStream), "UTF-8")));
    }

    public interface DataRead {
        Map<String, Integer> getHeader();

        void read(Consumer<Glob> consumer, GlobType globType);
    }

    static class DefaultDataRead implements DataRead {
        private CSVParser parse;

        public DefaultDataRead(CSVParser parse) {
            this.parse = parse;
        }

        public Map<String, Integer> getHeader() {
            return parse.getHeaderMap();
        }

        public void read(Consumer<Glob> consumer, GlobType globType) {
            ImportReaderBuilder readerBuilder = new ImportReaderBuilder(globType);
            Map<String, Integer> headerMap = parse.getHeaderMap();
            for (Map.Entry<String, Integer> stringIntegerEntry : headerMap.entrySet()) {
                Field field = findField(globType, stringIntegerEntry);
                if (field != null) {
                    readerBuilder.declare(field, stringIntegerEntry.getValue());
                }
                else {
                    LOGGER.warn(stringIntegerEntry.getKey() + " not used got : " + Arrays.toString(globType.getFields()));
                }
            }

            ImportReader build = readerBuilder.build();
            for (CSVRecord record : parse) {
                consumer.accept(build.read(record));
            }
        }

        private Field findField(GlobType globType, Map.Entry<String, Integer> stringIntegerEntry) {
            Field field = GlobTypeUtils.findNamedField(globType, stringIntegerEntry.getKey());
            if (field == null) {
                LOGGER.warn("Field " + stringIntegerEntry.getKey() + " ignored.");
            }
            return field;
        }
    }

    private CSVParser load(Reader reader) throws IOException {
        CSVFormat csvFormat =
                CSVFormat.DEFAULT
                        .withDelimiter(separator)
                        .withEscape('\\')
                        .withFirstRecordAsHeader();
        return csvFormat.parse(reader);
    }


    interface FieldReader {
        void read(MutableGlob mutableGlob, CSVRecord record);
    }

    static class ImportReaderBuilder {
        private List<FieldReader> fieldReaders = new ArrayList<>();
        private final GlobType type;

        ImportReaderBuilder(GlobType type) {
            this.type = type;
        }

        public void declare(Field field, Integer index) {
            field.safeVisit(new FieldVisitor.AbstractWithErrorVisitor() {
                public void visitInteger(IntegerField field) throws Exception {
                    fieldReaders.add(new IntegerFieldReader(field, index));
                }

                public void visitDouble(DoubleField field) throws Exception {
                    fieldReaders.add(new DoubleFieldReader(field, index));
                }

                public void visitString(StringField field) throws Exception {
                    fieldReaders.add(new StringFieldReader(field.hasAnnotation(ImportEmptyStringHasEmptyStringFormat.KEY), field, index));
                }

                public void visitLong(LongField field) throws Exception {
                    fieldReaders.add(new LongFieldReader(field, index));
                }

                public void visitDate(DateField field) throws Exception {
                    fieldReaders.add(new DateFieldReader(field, index));
                }
            });
        }

        ImportReader build() {
            return new ImportReader(fieldReaders.toArray(new FieldReader[0]), type);
        }
    }

    static class ImportReader {
        private final FieldReader[] fieldReaders;
        private final GlobType type;

        ImportReader(FieldReader[] fieldReaders, GlobType type) {
            this.fieldReaders = fieldReaders;
            this.type = type;
        }

        Glob read(CSVRecord record) {
            MutableGlob instantiate = type.instantiate();
            for (FieldReader fieldReader : fieldReaders) {
                fieldReader.read(instantiate, record);
            }
            return instantiate;
        }
    }

    static class IntegerFieldReader implements FieldReader {
        final IntegerField field;
        final int index;

        IntegerFieldReader(IntegerField field, int index) {
            this.field = field;
            this.index = index;
        }

        @Override
        public void read(MutableGlob mutableGlob, CSVRecord record) {
            String s = getValue(record, index);
            if (Strings.isNotEmpty(s)) {
                mutableGlob.set(field, Integer.parseInt(s.trim()));
            }
        }
    }

    static class LongFieldReader implements FieldReader {
        final LongField field;
        final int index;

        LongFieldReader(LongField field, int index) {
            this.field = field;
            this.index = index;
        }

        @Override
        public void read(MutableGlob mutableGlob, CSVRecord record) {
            String s = getValue(record, index);
            if (Strings.isNotEmpty(s)) {
                mutableGlob.set(field, Long.parseLong(s.trim()));
            }
        }
    }

    static class DateFieldReader implements FieldReader {
        final DateField field;
        final int index;
        private DateTimeFormatter dateTimeFormatter;

        DateFieldReader(DateField field, int index) {
            this.field = field;
            this.index = index;
            Glob dataFormat = field.findAnnotation(ExportDateFormat.KEY);
            if (dataFormat != null) {
                String s = dataFormat.get(ExportDateFormat.FORMAT);
                dateTimeFormatter = DateTimeFormatter.ofPattern(s);
            }
            else {
                dateTimeFormatter = DateTimeFormatter.ISO_DATE;
            }
        }

        public void read(MutableGlob mutableGlob, CSVRecord record) {
            String s = getValue(record, index);
            if (Strings.isNotEmpty(s)) {
                mutableGlob.set(field, (LocalDate) dateTimeFormatter.parse(s.trim()));
            }
        }
    }

    static class DoubleFieldReader implements FieldReader {
        final DoubleField field;
        final int index;

        DoubleFieldReader(DoubleField field, int index) {
            this.field = field;
            this.index = index;
        }

        @Override
        public void read(MutableGlob mutableGlob, CSVRecord record) {
            String s = getValue(record, index);
            if (Strings.isNotEmpty(s)) {
                mutableGlob.set(field, Double.parseDouble(s.trim()));
            }
        }
    }

    static class StringFieldReader implements FieldReader {
        final boolean emptyIsNotNull;
        final StringField field;
        final int index;

        StringFieldReader(boolean emptyIsNotNull, StringField field, int index) {
            this.emptyIsNotNull = emptyIsNotNull;
            this.field = field;
            this.index = index;
        }

        @Override
        public void read(MutableGlob mutableGlob, CSVRecord record) {
            String s = getValue(record, index);
            if (emptyIsNotNull || Strings.isNotEmpty(s)) {
                mutableGlob.set(field, s);
            }
        }

    }
    private static String getValue(CSVRecord record, int index) {
            String s = record.get(index);
//            if (s.length() > 2) {
//                if (s.startsWith("\"") && s.endsWith("\"")) {
//                    return s.substring(1, s.length() - 1);
//                }
//            }
            return s;
        }

}
