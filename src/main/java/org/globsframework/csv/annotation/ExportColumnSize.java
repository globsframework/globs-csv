package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class ExportColumnSize {
    public static final GlobType TYPE;

    public static final IntegerField SIZE;

    @InitUniqueKey
    public static final Key KEY;


    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ExportColumnSize");
        TYPE = typeBuilder.unCompleteType();
        SIZE = typeBuilder.declareIntegerField("size");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                .set(SIZE, ((ExportColumnSize_) annotation).value()));
        typeBuilder.complete();

        KEY = KeyBuilder.newEmptyKey(TYPE);

//        GlobTypeLoaderFactory.create(ExportColumnSize.class, "ExportColumnSize")
//                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
//                        .set(SIZE, ((ExportColumnSize_) annotation).value()))
//                .load();
    }
}
