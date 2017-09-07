package ru.shishmakov.core;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Named;
import javax.inject.Singleton;

public class GuiceModule extends AbstractModule {
    private final InputContext context;

    public GuiceModule(String[] args) {
        context = InputContext.build(args);
    }

    @Override
    protected void configure() {
        binder().install(new ExtAnnotationsModule());
        binder().bind(InputContext.class).toInstance(context);
    }

    @Provides
    @Singleton
    @Named("csv.mapper")
    public CsvMapper csvMapper() {
        return new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
    }

    @Provides
    @Singleton
    @Named("csv.readSchema")
    public CsvSchema readSchema() {
        return CsvSchema.emptySchema().withoutEscapeChar().withColumnSeparator(';');
    }

    @Provides
    @Singleton
    @Named("csv.writeSchema")
    public CsvSchema writeSchema() {
        return csvMapper().schemaFor(AppInstall.class).withColumnSeparator(';');
    }
}
