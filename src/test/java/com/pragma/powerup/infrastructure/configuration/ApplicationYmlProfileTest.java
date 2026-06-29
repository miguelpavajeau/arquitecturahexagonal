package com.pragma.powerup.infrastructure.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifica que el application.yml REAL (src/main/resources) resuelve la configuración
 * de JPA según el perfil activo. Usa una configuración vacía sin autoconfiguración,
 * por lo que no intenta conectarse a ninguna base de datos.
 */
class ApplicationYmlProfileTest {

    @Configuration
    static class EmptyConfig {
    }

    /**
     * Selecciona el perfil vía system property (misma precedencia que la variable de entorno
     * SPRING_PROFILES_ACTIVE usada en despliegue), de modo que sobreescriba el default 'dev'
     * declarado en el propio application.yml en lugar de sumarse a él.
     */
    private ConfigurableApplicationContext run(String profile) {
        String previous = System.getProperty("spring.profiles.active");
        System.setProperty("spring.profiles.active", profile);
        try {
            return new SpringApplicationBuilder(EmptyConfig.class)
                    .web(WebApplicationType.NONE)
                    .properties("spring.config.location=file:src/main/resources/application.yml")
                    .run();
        } finally {
            if (previous == null) {
                System.clearProperty("spring.profiles.active");
            } else {
                System.setProperty("spring.profiles.active", previous);
            }
        }
    }

    @Test
    void devProfile_enablesSchemaGenerationAndSql() {
        try (ConfigurableApplicationContext ctx = run("dev")) {
            Environment env = ctx.getEnvironment();
            assertEquals("update", env.getProperty("spring.jpa.hibernate.ddl-auto"));
            assertEquals("true", env.getProperty("spring.jpa.show-sql"));
        }
    }

    @Test
    void nonDevProfile_usesSafeDefaults() {
        try (ConfigurableApplicationContext ctx = run("prod")) {
            Environment env = ctx.getEnvironment();
            assertEquals("validate", env.getProperty("spring.jpa.hibernate.ddl-auto"));
            assertEquals("false", env.getProperty("spring.jpa.show-sql"));
        }
    }
}
