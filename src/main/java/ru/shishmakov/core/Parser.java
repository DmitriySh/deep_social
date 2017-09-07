package ru.shishmakov.core;

import java.util.List;
import java.util.Map;

public interface Parser {

    Map<AppInstall, Integer> from(String source) throws Exception;

    void to(List<AppInstall> listDTO, String path) throws Exception;
}
