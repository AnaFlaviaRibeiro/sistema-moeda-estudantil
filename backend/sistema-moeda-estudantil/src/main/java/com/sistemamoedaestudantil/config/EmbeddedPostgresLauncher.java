package com.sistemamoedaestudantil.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sobe um PostgreSQL 16 local sem depender do serviço do sistema nem do Docker.
 * Ativado no ambiente {@code dev} (padrão do {@code ./gradlew run}).
 */
public final class EmbeddedPostgresLauncher {

    private static final int PORT = 15432;
    private static EmbeddedPostgres instance;

    private EmbeddedPostgresLauncher() {
    }

    public static void startIfNeeded() {
        if (useExternalDatabase()) {
            return;
        }

        try {
            instance = EmbeddedPostgres.builder()
                    .setPort(PORT)
                    .start();
            initDatabase();
            applyDatasourceProperties();
            Runtime.getRuntime().addShutdownHook(new Thread(EmbeddedPostgresLauncher::stop, "embedded-pg-stop"));
            System.out.println("[dev] PostgreSQL 16 embutido em localhost:" + PORT + "/moeda_estudantil");
        } catch (IOException | SQLException ex) {
            throw new IllegalStateException("Não foi possível iniciar o PostgreSQL embutido.", ex);
        }
    }

    public static void stop() {
        if (instance != null) {
            try {
                instance.close();
            } catch (IOException ignored) {
                // shutdown
            }
            instance = null;
        }
    }

    private static boolean useExternalDatabase() {
        String env = System.getenv("MICRONAUT_ENVIRONMENTS");
        if (env == null || env.trim().isEmpty()) {
            env = System.getProperty("micronaut.environments", "dev");
        }
        if (env.contains("prod") || env.contains("external")) {
            return true;
        }
        return Boolean.parseBoolean(System.getProperty("db.external", "false"));
    }

    private static void initDatabase() throws SQLException {
        String adminUrl = instance.getJdbcUrl("postgres", "postgres");
        try (Connection connection = DriverManager.getConnection(adminUrl);
             Statement statement = connection.createStatement()) {
            statement.execute("DO $$ BEGIN CREATE ROLE moeda WITH LOGIN PASSWORD 'moeda'; "
                    + "EXCEPTION WHEN duplicate_object THEN NULL; END $$");
        }

        boolean dbExists;
        try (Connection connection = DriverManager.getConnection(adminUrl);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT 1 FROM pg_database WHERE datname = 'moeda_estudantil'")) {
            dbExists = rs.next();
        }

        if (!dbExists) {
            try (Connection connection = DriverManager.getConnection(adminUrl);
                 Statement statement = connection.createStatement()) {
                statement.execute("CREATE DATABASE moeda_estudantil OWNER moeda");
            }
        }
    }

    private static void applyDatasourceProperties() {
        System.setProperty("datasources.default.url",
                "jdbc:postgresql://localhost:" + PORT + "/moeda_estudantil");
        System.setProperty("datasources.default.username", "moeda");
        System.setProperty("datasources.default.password", "moeda");
        System.setProperty("datasources.default.driver-class-name", "org.postgresql.Driver");
        System.setProperty("datasources.default.dialect", "POSTGRES");
        System.setProperty("jpa.default.properties.hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect");
    }
}
