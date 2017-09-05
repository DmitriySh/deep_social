package ru.shishmakov.core;

import java.io.File;
import java.util.List;

public interface Parser {

    public List<AppInstall> from(String source) throws Exception;

    public File to(List<AppInstall> listDTO) throws Exception;
}
