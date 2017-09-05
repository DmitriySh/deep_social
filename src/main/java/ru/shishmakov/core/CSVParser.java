package ru.shishmakov.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
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

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

@Singleton
public class CSVParser implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
    private static final CsvSchema schema = CsvSchema.emptySchema().withoutEscapeChar().withColumnSeparator(';');

    @Override
    public List<AppInstall> from(String source) throws Exception {
        List<AppInstall> result = new ArrayList<>();
        MappingIterator<String[]> iterator = mapper.readerFor(String[].class).with(schema).readValues(new File(source));
        while (iterator.hasNext()) {
            String query = trimToEmpty(iterator.next()[7]);
            result.add(AppInstall.build(query));
        }
        return result;
    }

    @Override
    public File to(List<AppInstall> listDTO) throws JsonProcessingException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(AppInstall.class);
        String out = mapper.writer(schema).writeValueAsString(listDTO);
//        mapper.writeValue(new File("data.csv"), listDTO);

        return null; // TODO: 05.09.17 need response
    }

    public void n() throws IOException {
        Files.lines(new File("newfile.txt").toPath()).map(String::trim).filter(s -> !s.isEmpty()).forEach(System.out::println);

    }
}
