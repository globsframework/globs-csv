package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class CsvValueSeparator {
    public static final GlobType TYPE;

    public static final StringField SEPARATOR;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ValueSeparator");
        TYPE = typeBuilder.unCompleteType();
        SEPARATOR = typeBuilder.declareStringField("separator");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                .set(SEPARATOR, String.valueOf(((CsvValueSeparator_) annotation).value())));
        typeBuilder.complete();
        KEY = KeyBuilder.newEmptyKey(TYPE);
//        GlobTypeLoaderFactory.create(CsvValueSeparator.class, "ValueSeparator")
//                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
//                        .set(SEPARATOR, String.valueOf(((CsvValueSeparator_) annotation).value())))
//                .load();
    }
}
