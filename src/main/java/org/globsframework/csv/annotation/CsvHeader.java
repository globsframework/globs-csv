package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.BooleanField;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class CsvHeader {
    public static final GlobType TYPE;

    public static final StringField name;

    public static final BooleanField firstLineIsHeader;

//    public static BooleanField noHeader;

//    public static StringArrayField header;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("CsvHeader");
        TYPE = typeBuilder.unCompleteType();
        name = typeBuilder.declareStringField("name");
        firstLineIsHeader = typeBuilder.declareBooleanField("firstLineIsHeader");
        typeBuilder.complete();
        KEY = KeyBuilder.newEmptyKey(TYPE);
        typeBuilder
                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                        .set(name, ((CsvHeader_) annotation).value())
                        .set(firstLineIsHeader, ((CsvHeader_) annotation).firstLineIsHeader()));


//        GlobTypeLoaderFactory.create(CsvHeader.class, "CsvHeader")
//                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
//                                .set(name, ((CsvHeader_) annotation).value())
//                                .set(firstLineIsHeader, ((CsvHeader_) annotation).firstLineIsHeader())
//                        .set(noHeader, ((CsvHeader_) annotation).noHeader())
//                        .set(header, ((CsvHeader_) annotation).header())
//                )
//                .load();
    }
}
