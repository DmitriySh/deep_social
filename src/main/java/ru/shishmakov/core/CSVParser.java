package ru.shishmakov.core;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

@Singleton
public class CSVParser implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
    private static final CsvSchema readSchema = CsvSchema.emptySchema().withoutEscapeChar().withColumnSeparator(';');
    private static final CsvSchema writeSchema = mapper.schemaFor(AppInstall.class).withColumnSeparator(';');

    @Override
    public List<AppInstall> from(String source) throws Exception {
        List<AppInstall> result = new ArrayList<>();
        MappingIterator<String[]> iterator = mapper.readerFor(String[].class).with(readSchema).readValues(new File(source));
        while (iterator.hasNext()) {
            String query = trimToEmpty(iterator.next()[7]);
            result.add(AppInstall.build(query));
        }
        logger.info("Read csv file: {}, lines: {}", source, result.size());
        return result;
    }

    @Override
    public void to(List<AppInstall> listDTO, String path) throws IOException {
        byte[] bytes = mapper.writer(writeSchema).writeValueAsBytes(listDTO);
        try (OutputStream file = new FileOutputStream(path);
             OutputStream bout = new BufferedOutputStream(file)) {
            bout.write(bytes);
        }
        logger.info("Read csv file: {}, lines: {}", path, listDTO.size());
    }

    public void n() throws IOException {
        Files.lines(new File("newfile.txt").toPath()).map(String::trim).filter(s -> !s.isEmpty()).forEach(System.out::println);

    }
}
