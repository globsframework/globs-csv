package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class ExportBooleanFormat {
    public static final GlobType TYPE;

    public static final StringField TRUE_;

    public static final StringField FALSE_;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ExportBooleanFormat");
        TYPE = typeBuilder.unCompleteType();
        TRUE_ = typeBuilder.declareStringField("true");
        FALSE_ = typeBuilder.declareStringField("false");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                .set(TRUE_, ((ExportBooleanFormat_) annotation).true_())
                .set(FALSE_, ((ExportBooleanFormat_) annotation).false_())
        );
        typeBuilder.complete();

        KEY = KeyBuilder.newEmptyKey(TYPE);

//        GlobTypeLoaderFactory.create(ExportBooleanFormat.class, "ExportBooleanFormat")
//                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
//                        .set(TRUE_, ((ExportBooleanFormat_) annotation).true_())
//                        .set(FALSE_, ((ExportBooleanFormat_) annotation).false_())
//                )
//                .load();
    }
}
