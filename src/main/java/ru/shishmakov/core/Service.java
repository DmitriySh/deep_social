package ru.shishmakov.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.core.InputContext.DataType;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.shishmakov.core.InputContext.DataType.CSV;

public class Service {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Map<DataType, Function<String, List<AppInstall>>> reader = new HashMap<>();
    private final Map<DataType, Function<List<AppInstall>, Void>> writer = new HashMap<>();

    @Inject
    private InputContext context;
    @Inject
    private CSVParser parser;

    @PostConstruct
    public void before() {
        reader.put(CSV, getCSVReader());
        writer.put(CSV, getCSVWriter());
    }

    public Service start() {
        logger.info("Service started");

        process();
        logger.info("Service stopped");
        return this;
    }

    private void process() {
        try {
            Map<AppInstall, Integer> groups = new HashMap<>();
            reader.getOrDefault(context.type, (a) -> Collections.emptyList())
                    .apply(context.source)
                    .forEach(app -> groups.merge(app, 1, (a, b) -> a + b));

            List<AppInstall> installs = groups.entrySet().stream()
                    .peek(e -> e.getKey().setQuantity(e.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            writer.getOrDefault(context.type, (a) -> null)
                    .apply(installs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Parse error", e);
        }
    }

    private Function<String, List<AppInstall>> getCSVReader() {
        return (String path) -> {
            try {
                return parser.from(path);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<List<AppInstall>, Void> getCSVWriter() {
        return installs -> {
            try {
                parser.to(installs, context.dest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        };
    }
}
