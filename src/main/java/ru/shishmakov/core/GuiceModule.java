package ru.shishmakov.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiceModule extends AbstractModule {
    private final InputContext context;

    public GuiceModule(String[] args) {
        context = InputContext.build(args);
    }

    @Override
    protected void configure() {
//        binder().install(new ExtAnnotationsModule()); // todo if will need
        binder().bind(InputContext.class).toInstance(context);
    }

    @Provides
    @Singleton
    @Named("social.executor")
    public ExecutorService elevatorExecutor() {
        ThreadFactoryBuilder factory = new ThreadFactoryBuilder();
        factory.setNameFormat("social.executor" + "-%d");
        return Executors.newCachedThreadPool(factory.build());
    }
}
