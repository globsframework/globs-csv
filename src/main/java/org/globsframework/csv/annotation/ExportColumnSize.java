package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;
import org.globsframework.core.model.MutableGlob;

public class ExportColumnSize {
    public static final GlobType TYPE;

    public static final IntegerField SIZE;

    @InitUniqueKey
    public static final Key KEY;


    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ExportColumnSize");
        SIZE = typeBuilder.declareIntegerField("size");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> getMutableGlob((ExportColumnSize_) annotation));
        TYPE = typeBuilder.build();

        KEY = KeyBuilder.newEmptyKey(TYPE);
    }

    private static MutableGlob getMutableGlob(ExportColumnSize_ annotation) {
        return TYPE.instantiate()
                .set(SIZE, annotation.value());
    }

    public static Glob create(int size) {
        return TYPE.instantiate().set(SIZE, size);
    }
}
