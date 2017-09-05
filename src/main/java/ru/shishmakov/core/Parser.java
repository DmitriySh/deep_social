package ru.shishmakov.core;

import java.io.File;
import java.util.List;

public interface Parser {

    List<AppInstall> from(String source) throws Exception;

    File to(List<AppInstall> listDTO) throws Exception;
}
