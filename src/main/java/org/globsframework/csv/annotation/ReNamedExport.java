package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.annotations.Target;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.metamodel.fields.GlobArrayField;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;
import org.globsframework.core.utils.Strings;

import java.util.Arrays;
import java.util.Optional;

public class ReNamedExport {
    public static final GlobType TYPE;

    @Target(Mapping.class)
    public static final GlobArrayField names;

    public static final StringField defaultValue;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ReNamedExport");
        TYPE = typeBuilder.unCompleteType();
        names = typeBuilder.declareGlobArrayField("names", Mapping.TYPE);
        defaultValue = typeBuilder.declareStringField("defaultValue");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                .set(names, Arrays.stream(((ReNamedExport_) annotation).multi())
                        .map(Mapping::create)
                        .toArray(Glob[]::new))
                .set(defaultValue, ((ReNamedExport_) annotation).value())
        );
        typeBuilder.complete();
        KEY = KeyBuilder.newEmptyKey(TYPE);
//        GlobTypeLoaderFactory.create(ReNamedExport.class, "ReNamedExport")
//                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
//                        .set(names, Arrays.stream(((ReNamedExport_) annotation).multi())
//                                .map(Mapping::create)
//                                .toArray(Glob[]::new))
//                        .set(defaultValue, ((ReNamedExport_) annotation).value())
//                )
//                .load();
    }

    public static String getHeaderName(String name, Field field) {
        Optional<Glob> annotation = field.findOptAnnotation(KEY);
        if (annotation.isPresent()) {
            if (Strings.isNotEmpty(name)) {
                Glob[] renamed = annotation.get().getOrEmpty(names);
                for (Glob glob : renamed) {
                    if (glob.get(Mapping.name).equals(name)) {
                        return glob.get(Mapping.renamed);
                    }
                }
            }
            String def = annotation.get().get(defaultValue);
            if (Strings.isNotEmpty(def)) {
                return def;
            }
        }
        return field.getName();
    }

    public static class Mapping {
        public static final GlobType TYPE;

        public static final StringField name;

        public static final StringField renamed;

        static {
            GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("Mapping");
            TYPE = typeBuilder.unCompleteType();
            name = typeBuilder.declareStringField("name");
            renamed = typeBuilder.declareStringField("renamed");
            typeBuilder.complete();
//            GlobTypeLoaderFactory.create(Mapping.class, "Mapping").load();
        }

        public static Glob create(ReNamedMappingExport_ mapping) {
            return TYPE.instantiate()
                    .set(name, mapping.name())
                    .set(renamed, mapping.to())
                    ;
        }
    }
}
