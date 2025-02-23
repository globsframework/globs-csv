package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringArrayField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class NamedExport {
    public static final GlobType TYPE;

    public static final StringArrayField names;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("NamedExport");
        TYPE = typeBuilder.unCompleteType();
        names = typeBuilder.declareStringArrayField("names");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                .set(names, ((NamedExport_) annotation).value())
        );
        typeBuilder.complete();
        KEY = KeyBuilder.newEmptyKey(TYPE);
//        GlobTypeLoaderFactory.create(NamedExport.class, "NamedExport")
//                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
//                        .set(names, ((NamedExport_) annotation).value())
//                )
//                .load();
    }
}
