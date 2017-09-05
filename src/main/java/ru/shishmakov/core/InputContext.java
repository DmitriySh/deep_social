package ru.shishmakov.core;

import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

public class InputContext {
    public final DataType type;
    public final String source;
    public final String dest;

    private InputContext(DataType type, String path, String dest) {
        this.type = type;
        this.source = path;
        this.dest = dest;
    }

    public static InputContext build(String[] params) {
        checkArgument(params.length == 3, "Input should have 3 params: 1) Data type (db, csv, json); 2) Source path; 3) Destination path");
        checkArgument(StringUtils.isNotBlank(params[0]), "Data type should be: (db, csv, json)");
        checkArgument(StringUtils.isNotBlank(params[1]), "Source path should not be empty");
        checkArgument(StringUtils.isNotBlank(params[2]), "Destination path should not be empty");

        return new InputContext(DataType.valueOf(params[0].toUpperCase()), params[1], params[2]);
    }

    enum DataType {
        DB,
        CSV,
        JSON
    }
}
