package com.sistemamoedaestudantil;

import com.sistemamoedaestudantil.config.EmbeddedPostgresLauncher;
import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        EmbeddedPostgresLauncher.startIfNeeded();
        Micronaut.run(Application.class, args);
    }
}