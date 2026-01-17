package org.globsframework.csv;

import org.globsframework.core.metamodel.fields.Field;

public interface HeaderToField {
    Field getField(String headerName);
}
