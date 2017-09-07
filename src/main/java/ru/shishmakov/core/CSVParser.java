package ru.shishmakov.core;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

@Singleton
public class CSVParser implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    @Named("csv.mapper")
    private CsvMapper mapper;
    @Inject
    @Named("csv.readSchema")
    private CsvSchema readSchema;
    @Inject
    @Named("csv.writeSchema")
    private CsvSchema writeSchema;

    @Override
    public Map<AppInstall, Integer> from(String source) throws Exception {
        Map<AppInstall, Integer> groups = new HashMap<>();
        MappingIterator<String[]> iterator = mapper.readerFor(String[].class).with(readSchema).readValues(new File(source));
        int count = 0;
        while (iterator.hasNext()) {
            String query = trimToEmpty(iterator.next()[7]);
            groups.merge(AppInstall.build(query), 1, (a, b) -> a + b);
            count += 1;
        }
        logger.info("Read csv file: {}, lines: {}", source, count);
        return groups;
    }

    @Override
    public void to(List<AppInstall> listDTO, String path) throws IOException {
        byte[] bytes = mapper.writer(writeSchema).writeValueAsBytes(listDTO);
        try (OutputStream file = new FileOutputStream(path);
             OutputStream bout = new BufferedOutputStream(file)) {
            bout.write(bytes);
        }
        logger.info("Write csv file: {}, lines: {}", path, listDTO.size());
    }

    public void n() throws IOException {
        Files.lines(new File("newfile.txt").toPath()).map(String::trim).filter(s -> !s.isEmpty()).forEach(System.out::println);

    }
}
