package ru.shishmakov.core;

import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author <a href="mailto:d.shishmakov@corp.nekki.ru">Shishmakov Dmitriy</a>
 */
public class InputContext {
    public final DataType type;
    public final String path;

    private InputContext(DataType type, String path) {
        this.type = type;
        this.path = path;
    }

    public static InputContext build(String[] params) {
        checkArgument(params.length == 2, "Input should have 2 params: 1) Data type (db, csv, json); 2) Path");
        checkArgument(StringUtils.isNotBlank(params[0]), "Data type should be: (db, csv, json)");
        checkArgument(StringUtils.isNotBlank(params[1]), "Path should not be empty");

        return new InputContext(DataType.valueOf(params[0].toUpperCase()), params[1]);
    }

    enum DataType {
        DB,
        CSV,
        JSON
    }
}
