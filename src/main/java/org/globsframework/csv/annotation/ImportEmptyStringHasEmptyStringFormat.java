package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.BooleanField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class ImportEmptyStringHasEmptyStringFormat {
    public static final GlobType TYPE;

    public static final BooleanField EMPTY_STRING;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ImportEmptyStringHasEmptyStringFormat");
        TYPE = typeBuilder.unCompleteType();
        EMPTY_STRING = typeBuilder.declareBooleanField("emptyString");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                .set(EMPTY_STRING, ((ImportEmptyStringHasEmptyStringFormat_) annotation).value())
        );
        typeBuilder.complete();
        KEY = KeyBuilder.newEmptyKey(TYPE);
//        GlobTypeLoaderFactory.create(ImportEmptyStringHasEmptyStringFormat.class, "ImportEmptyStringHasEmptyStringFormat")
//                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
//                        .set(EMPTY_STRING, ((ImportEmptyStringHasEmptyStringFormat_) annotation).value())
//                )
//                .load();
    }
}
