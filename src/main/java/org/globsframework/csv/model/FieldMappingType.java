package org.globsframework.csv.model;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.Target;
import org.globsframework.core.metamodel.annotations.Targets;
import org.globsframework.core.metamodel.fields.*;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Glob;
import org.globsframework.json.annottations.IsJsonContent;
import org.globsframework.json.annottations.IsJsonContent_;

import java.util.List;

public class FieldMappingType {
    public static final GlobType TYPE;

    @Targets({FromType.class, TemplateType.class, SumData.class, OverrideData.class, MappingData.class, JoinType.class})
    public static final GlobUnionField from;

    public static final StringField to;

    public static final StringField targetType; //default string

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("FieldMapping");
        TYPE = typeBuilder.unCompleteType();
        from = typeBuilder.declareGlobUnionField("from",
                List.of(FromType.TYPE, TemplateType.TYPE, SumData.TYPE, OverrideData.TYPE, MappingData.TYPE, JoinType.TYPE));
        to = typeBuilder.declareStringField("to");
        targetType = typeBuilder.declareStringField("targetType");
        typeBuilder.complete();

//        GlobTypeLoaderFactory.create(FieldMappingType.class).load();
    }

//  TODO   {a1,a2}{b}{c1,c2} => a1.b.c1, a1.b.c2, a1.b.c1, a1.b.c2

    public static class SumData {
        public static final GlobType TYPE;

        @Target(FromType.class)
        public static final GlobArrayField from;

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("SumData");
            TYPE = typeBuilder.unCompleteType();
            from = typeBuilder.declareGlobArrayField("from", FromType.TYPE);
            typeBuilder.complete();
            //  GlobTypeLoaderFactory.create(SumData.class).load();
        }
    }

    public static class OverrideData {
        public static final GlobType TYPE;

        public static final StringField name;

        @Target(FromType.class)
        public static final GlobArrayField inputField;

        @IsJsonContent_
        public static final StringField additionalParams;

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("OverrideData");
            TYPE = typeBuilder.unCompleteType();
            name = typeBuilder.declareStringField("name");
            inputField = typeBuilder.declareGlobArrayField("inputField", FromType.TYPE);
            additionalParams = typeBuilder.declareStringField("additionalParams", IsJsonContent.UNIQUE_GLOB);
            typeBuilder.complete();

//            GlobTypeLoaderFactory.create(OverrideData.class).load();
        }
    }

    public static class FromType {
        public static final GlobType TYPE;

        public static final StringField from;

        public static final StringField defaultValueIfEmpty;

        public static final StringField toStringFormater;

        @Target(FormatType.class)
        public static final GlobArrayField formater;

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("FromType");
            TYPE = typeBuilder.unCompleteType();
            from = typeBuilder.declareStringField("from");
            defaultValueIfEmpty = typeBuilder.declareStringField("defaultValueIfEmpty");
            toStringFormater = typeBuilder.declareStringField("toStringFormater");
            formater = typeBuilder.declareGlobArrayField("formater", FormatType.TYPE);
            typeBuilder.complete();
//            GlobTypeLoaderFactory.create(FromType.class).load();
        }
    }

    public static class RenamedType {

        public static final GlobType TYPE;

        @Target(FromType.class)
        public static final GlobField from;

        public static final StringField renameTo; // par defaut identique a from

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("RenamedType");
            TYPE = typeBuilder.unCompleteType();
            from = typeBuilder.declareGlobField("from", FromType.TYPE);
            renameTo = typeBuilder.declareStringField("renameTo");
            typeBuilder.complete();
//            GlobTypeLoaderFactory.create(RenamedType.class).load();
        }
    }

    public static class JoinType {
        public static final GlobType TYPE;

        @Target(FromType.class)
        public static final GlobArrayField from;

        public static final StringField separator;

        public static final StringField first;

        public static final BooleanField addFirstIfEmpty;

        public static final StringField last;

        public static final BooleanField addLastIfEmpty;

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("Join");
            TYPE = typeBuilder.unCompleteType();
            from = typeBuilder.declareGlobArrayField("from", FromType.TYPE);
            separator = typeBuilder.declareStringField("separator");
            first = typeBuilder.declareStringField("first");
            addFirstIfEmpty = typeBuilder.declareBooleanField("addFirstIfEmpty");
            last = typeBuilder.declareStringField("last");
            addLastIfEmpty = typeBuilder.declareBooleanField("addLastIfEmpty");
            typeBuilder.complete();
//            GlobTypeLoaderFactory.create(JoinType.class).load();
        }
    }

    public static class TemplateType {
        public static final GlobType TYPE;

        @Target(RenamedType.class)
        public static final GlobArrayField from;

        public static final StringField template;

        public static final BooleanField noValueIfOnIsMissing;

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("Template");
            TYPE = typeBuilder.unCompleteType();
            from = typeBuilder.declareGlobArrayField("from", RenamedType.TYPE);
            template = typeBuilder.declareStringField("template");
            noValueIfOnIsMissing = typeBuilder.declareBooleanField("noValueIfOnIsMissing");
            typeBuilder.complete();
//            GlobTypeLoaderFactory.create(TemplateType.class).load();
        }
    }

    public static class FormatType {
        public static final GlobType TYPE;

        public static final StringField matcher;

        public static final StringField result;

        // TODO
//        @EnumAnnotation("")
//        public static StringField valueIfNoMatch;

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("FormatType");
            TYPE = typeBuilder.unCompleteType();
            matcher = typeBuilder.declareStringField("matcher");
            result = typeBuilder.declareStringField("result");
            typeBuilder.complete();
//            GlobTypeLoaderFactory.create(FormatType.class).load();
        }
    }

    public static class MappingData {
        public static final GlobType TYPE;

        public static final StringField mappingName;

        @Target(FromType.class)
        public static final GlobField from;

        public static final BooleanField copyValueIfNoMapping;

        public static final StringField defaultValueNoMapping;

        public static final StringField defaultEmptyValue;

        @Target(KeyValue.class)
        public static final GlobArrayField mapping;

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("MappingData");
            TYPE = typeBuilder.unCompleteType();
            mappingName = typeBuilder.declareStringField("mappingName");
            from = typeBuilder.declareGlobField("from", FromType.TYPE);
            copyValueIfNoMapping = typeBuilder.declareBooleanField("copyValueIfNoMapping");
            defaultValueNoMapping = typeBuilder.declareStringField("defaultValueNoMapping");
            defaultEmptyValue = typeBuilder.declareStringField("defaultEmptyValue");
            mapping = typeBuilder.declareGlobArrayField("mapping", KeyValue.TYPE);
            typeBuilder.complete();
//            GlobTypeLoaderFactory.create(MappingData.class).load();
        }
    }

    public static class KeyValue {
        public static final GlobType TYPE;

        public static final StringField key;

        public static final StringField value;

        public static Glob create(String key, String value) {
            return TYPE.instantiate().set(KeyValue.key, key).set(KeyValue.value, value);
        }

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("KeyValue");
            TYPE = typeBuilder.unCompleteType();
            key = typeBuilder.declareStringField("key");
            value = typeBuilder.declareStringField("value");
            typeBuilder.complete();
//            GlobTypeLoaderFactory.create(KeyValue.class).load();
        }
    }
}
