package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;
import org.globsframework.core.model.MutableGlob;

public class ExportBooleanFormat {
    public static final GlobType TYPE;

    public static final StringField TRUE_;

    public static final StringField FALSE_;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ExportBooleanFormat");
        TRUE_ = typeBuilder.declareStringField("true");
        FALSE_ = typeBuilder.declareStringField("false");
        typeBuilder.register(GlobCreateFromAnnotation.class,
                annotation -> getMutableGlob((ExportBooleanFormat_) annotation));
        TYPE = typeBuilder.build();

        KEY = KeyBuilder.newEmptyKey(TYPE);
    }

    private static MutableGlob getMutableGlob(ExportBooleanFormat_ annotation) {
        return TYPE.instantiate()
                .set(TRUE_, annotation.true_())
                .set(FALSE_, annotation.false_());
    }
}
