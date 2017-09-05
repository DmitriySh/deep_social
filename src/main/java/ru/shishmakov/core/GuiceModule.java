package ru.shishmakov.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiceModule extends AbstractModule {

    public GuiceModule(String[] args) {
        // todo need parse
    }

    @Override
    protected void configure() {
        binder().install(new ExtAnnotationsModule());
    }

    @Provides
    @Singleton
    @Named("social.executor")
    public ExecutorService elevatorExecutor() {
        ThreadFactoryBuilder factory = new ThreadFactoryBuilder();
        factory.setNameFormat("social.executor" + "-%d");
//        factory.setUncaughtExceptionHandler((t, e) -> logger.warn("thread pool: " + name + " has unhandled exception", e));
        return Executors.newCachedThreadPool(factory.build());
    }
}
