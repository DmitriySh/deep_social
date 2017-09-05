package ru.shishmakov;

import com.google.inject.Guice;
import ru.shishmakov.core.GuiceModule;
import ru.shishmakov.core.Service;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Guice.createInjector(new GuiceModule(args))
                .getInstance(Service.class)
                .start();
    }
}
