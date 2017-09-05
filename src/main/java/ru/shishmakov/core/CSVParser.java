package ru.shishmakov.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class CSVParser implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public List<AppInstall> from(String source) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(AppInstall.class)
                .withNullValue("NULL")
                .withoutEscapeChar()
                .withColumnSeparator(';');
        List<AppInstall> result = new ArrayList<>();
        MappingIterator<AppInstall> it = mapper.readerFor(AppInstall.class).with(schema).readValues(new File(source));
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    @Override
    public File to(List<AppInstall> listDTO) throws JsonProcessingException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(AppInstall.class).withHeader();
        String out = mapper.writer(schema).writeValueAsString(listDTO);

        return null; // TODO: 05.09.17 need response
    }

    public void n() throws IOException {
        Files.lines(new File("newfile.txt").toPath()).map(String::trim).filter(s -> !s.isEmpty()).forEach(System.out::println);

    }
}
