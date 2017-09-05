package ru.shishmakov.core;

import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static ru.shishmakov.util.Threads.sleepWithoutInterruptedAfterTimeout;

public class Service {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AtomicBoolean watcherState = new AtomicBoolean(true);
    @Inject
    @Named("social.executor")
    private ExecutorService executor;

    public Service() {
        this.executor = Executors.newCachedThreadPool();
    }

    public Service start() {
        logger.info("Service starting...");

        assignThreadHook(this::stop, "server-main-hook-thread");
//        executor.execute(this::???);
        logger.info("Service started");
        return this;
    }

    public void stop() {
        logger.info("Service stopping...");

        watcherState.compareAndSet(true, false);
        stopExecutors();
        logger.info("Service stopped, state");
    }

    public void await() throws InterruptedException {
        logger.info("Service thread: {} await termination", Thread.currentThread());
        for (long count = 0; watcherState.get() && !Thread.currentThread().isInterrupted(); count++) {
            if (count % 100 == 0) logger.debug("Thread: {} is alive", Thread.currentThread());
            sleepWithoutInterruptedAfterTimeout(100, MILLISECONDS);
        }
    }

    private void stopExecutors() {
        logger.info("Service executor stopping...");
        try {
            MoreExecutors.shutdownAndAwaitTermination(executor, 2, SECONDS);
            logger.info("Executor services stopped");
        } catch (Exception e) {
            logger.error("Service exception occurred during stopping executor services", e);
        }
    }

    public static void assignThreadHook(Runnable task, String name) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.debug("{} was interrupted by hook", Thread.currentThread());
            task.run();
        }, name));
    }

}
