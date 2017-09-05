package ru.shishmakov.core;

import com.google.inject.AbstractModule;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

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
}
