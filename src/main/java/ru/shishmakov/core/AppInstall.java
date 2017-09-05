package ru.shishmakov.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

@JsonPropertyOrder(value = {"name", "dob"})
public class AppInstall {
    private Date dob;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date getDob() {
        return dob;
    }

    public String getName() {
        return name;
    }
    //setters etc...
}
