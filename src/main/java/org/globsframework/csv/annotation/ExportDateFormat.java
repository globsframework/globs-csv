package org.globsframework.csv.annotation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class ExportDateFormat {
    public static final GlobType TYPE;

    public static final StringField FORMAT;

    public static final StringField ZONE_ID;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ExportDateFormat");
        TYPE = typeBuilder.unCompleteType();
        FORMAT = typeBuilder.declareStringField("format");
        ZONE_ID = typeBuilder.declareStringField("zoneId");
        typeBuilder
                .register(GlobCreateFromAnnotation.class, annotation -> {
                            String zoneId = ((ExportDateFormat_) annotation).zoneId();
                            return TYPE.instantiate()
                                    .set(FORMAT, ((ExportDateFormat_) annotation).value())
                                    .set(ZONE_ID, zoneId.isEmpty() ? null : zoneId);
                        }
                );
        typeBuilder.complete();
        KEY = KeyBuilder.newEmptyKey(TYPE);

//        GlobTypeLoaderFactory.create(ExportDateFormat.class, "ExportDateFormat")
//                .register(GlobCreateFromAnnotation.class, annotation -> {
//                            String zoneId = ((ExportDateFormat_) annotation).zoneId();
//                            return TYPE.instantiate()
//                                    .set(FORMAT, ((ExportDateFormat_) annotation).value())
//                                    .set(ZONE_ID, zoneId.isEmpty() ? null : zoneId);
//                        }
//                )
//                .load();
    }
}
