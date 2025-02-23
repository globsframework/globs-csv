package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class ExportDoubleFormat {
    public static final GlobType TYPE;

    public static final StringField FORMAT;

    public static final StringField DECIMAL_SEPARATOR;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ExportDoubleFormat");
        TYPE = typeBuilder.unCompleteType();
        FORMAT = typeBuilder.declareStringField("format");
        DECIMAL_SEPARATOR = typeBuilder.declareStringField("decimalSeparator");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation ->
                TYPE.instantiate()
                        .set(FORMAT, ((ExportDoubleFormat_) annotation).format())
                        .set(DECIMAL_SEPARATOR, ((ExportDoubleFormat_) annotation).decimalSeparator())
        );
        typeBuilder.complete();
        KEY = KeyBuilder.newEmptyKey(TYPE);
//        GlobTypeLoaderFactory.create(ExportDoubleFormat.class, "ExportDoubleFormat")
//                .load();
    }
}
