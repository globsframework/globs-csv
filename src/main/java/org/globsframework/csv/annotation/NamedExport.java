package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringArrayField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;
import org.globsframework.core.model.MutableGlob;

public class NamedExport {
    public static final GlobType TYPE;

    public static final StringArrayField names;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("NamedExport");
        names = typeBuilder.declareStringArrayField("names");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> getMutableGlob((NamedExport_) annotation));
        TYPE = typeBuilder.build();
        KEY = KeyBuilder.newEmptyKey(TYPE);
    }

    private static MutableGlob getMutableGlob(NamedExport_ annotation) {
        return TYPE.instantiate()
                .set(names, annotation.value());
    }

    public static Glob create(String ...names) {
        return TYPE.instantiate().set(NamedExport.names, names);
    }
}
