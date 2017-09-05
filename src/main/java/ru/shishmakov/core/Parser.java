package ru.shishmakov.core;

import java.util.List;

public interface Parser {

    List<AppInstall> from(String source) throws Exception;

    void to(List<AppInstall> listDTO, String path) throws Exception;
}
