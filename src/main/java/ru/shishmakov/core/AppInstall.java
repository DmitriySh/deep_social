package ru.shishmakov.core;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

@JsonPropertyOrder(value = {"ip", "date", "internal", "uri", "code1", "code2", "locate", "parameters", "endLine"})
public class AppInstall {
    public String ip;
    public String date;
    public String internal;
    public String uri;
    public String code1;
    public String code2;
    public String locate;
    public String parameters;
    public String endLine; // TODO: 05.09.17 fix: do not use unnecessary fields

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("ip", ip)
                .add("date", date)
                .add("internal", internal)
                .add("uri", uri)
                .add("code1", code1)
                .add("code2", code2)
                .add("locate", locate)
                .add("parameters", parameters)
                .toString();
    }
}
