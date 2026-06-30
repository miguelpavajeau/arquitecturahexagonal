# Guía de Migración: Spring Boot 2.7 → 3.3 + Java 11 → 21

## Resumen de cambios

Este documento describe todos los cambios realizados para actualizar el proyecto de Spring Boot 2.7.3 a Spring Boot 3.3.0 y Java 11 a Java 21.

## Cambios en `build.gradle`

### Versiones de plugins y dependencias

| Componente | Antes | Después | Cambio |
|-----------|-------|---------|--------|
| Spring Boot Plugin | 2.7.3 | 3.3.0 | +1.5.7 |
| Dependency Management | 1.0.13.RELEASE | 1.1.4 | +0.1.1 |
| Java Compatibility | 11 | 21 | +10 (LTS) |
| MySQL Connector | 8.0.32 | 8.0.33 | +0.0.1 |
| OpenAPI Springdoc | 1.6.11 → 2.3.0 | Cambio de API |
| JJWT | 0.11.5 | 0.12.3 | Cambio de API |

### Cambios en dependencias

- **Removida**: `springdoc-openapi-webflux-ui` (no es necesaria para Spring MVC)
- **Actualizada**: `springdoc-openapi-ui` → `springdoc-openapi-starter-webmvc-ui` (nueva estructura en v2.x)

## Cambios en el código

### 1. Namespace Jakarta (javax → jakarta)

En Spring Boot 3.x, todos los namespaces `javax.*` fueron reemplazados por `jakarta.*` como parte de la transición de Java EE a Jakarta EE.

**Cambios realizados:**
- `javax.persistence.*` → `jakarta.persistence.*` (JPA)
- `javax.validation.*` → `jakarta.validation.*` (Validation)
- `javax.transaction.*` → `jakarta.transaction.*` (Transactions)
- `javax.servlet.*` → `jakarta.servlet.*` (Servlet API)

**Nota**: `javax.crypto.*` no se cambió porque es parte del JDK estándar, no de Java EE.

**Ejemplo:**
```java
// Antes
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

// Después
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
```

### 2. Spring Security - Nueva sintaxis

Spring Boot 3.x usa Spring Security 6, que cambió la API de configuración.

**Archivo**: `SecurityConfiguration.java`

**Cambios:**
- `@EnableGlobalMethodSecurity` → `@EnableMethodSecurity`
- `antMatchers()` → `requestMatchers()`
- `authorizeRequests()` → `authorizeHttpRequests()`
- Sintaxis de chains con `.and()` → lambda-based configuration

**Ejemplo:**
```java
// Antes (Spring Security 5)
http.csrf().disable()
    .cors().configurationSource(corsConfigurationSource())
    .and()
    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .and()
    .authorizeRequests()
    .antMatchers(HttpMethod.POST, "/api/v1/user/client").permitAll()
    .anyRequest().authenticated();

// Después (Spring Security 6)
http
    .csrf(csrf -> csrf.disable())
    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(authz -> authz
        .requestMatchers(HttpMethod.POST, "/api/v1/user/client").permitAll()
        .anyRequest().authenticated()
    );
```

### 3. JJWT - Cambio de API (0.11 → 0.12)

La librería JJWT cambió su API significativamente en v0.12.x.

**Archivo**: `JwtProvider.java`

**Cambios:**
- `Jwts.parserBuilder()` → `Jwts.parser()`
- `.setSigningKey(key)` → `.verifyWith(key)` (con tipo `SecretKey`)
- `.parseClaimsJws(token)` → `.parseSignedClaims(token)`
- `.getBody()` → `.getPayload()`
- Tipo de key: `Key` → `SecretKey`

**Ejemplo:**
```java
// Antes (JJWT 0.11)
private Claims parseClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
}

// Después (JJWT 0.12)
private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
}
```

## Beneficios de la actualización

### Java 21 LTS
- **Mejor rendimiento**: Optimizaciones de JVM, garbage collection mejorado
- **Seguridad**: Parches de seguridad más recientes
- **Virtual Threads**: Disponibles en preview (mejor escalabilidad)
- **Pattern Matching**: Mejor manejo de tipos en condicionales
- **Record Classes**: Mejor para DTOs (ya disponible desde Java 16)

### Spring Boot 3.3
- **Native Image Support**: Compilación a binario nativo con GraalVM
- **Observabilidad**: Integración mejorada con micrometer y trazas distribuidas
- **Performance**: Mejor arranque y menor consumo de memoria
- **Seguridad**: Spring Security 6 con nuevas características
- **OpenAPI 3.1**: Mejor soporte de especificación OpenAPI

## Pasos de instalación

### Requisitos

1. **Java 21 LTS** (mínimo Java 17)
   ```bash
   # Descargar desde: https://jdk.java.net/21
   # O usar sdkman: sdk install java 21-open
   ```

2. **Gradle 8.5+** (ya está especificado en gradlew)
   ```bash
   ./gradlew --version  # Debería mostrar 8.5 o superior
   ```

### Compilación

```bash
# Limpiar y compilar
./gradlew clean build

# Ejecutar tests
./gradlew test

# Ejecutar la aplicación
./gradlew bootRun
```

## Verificación de compatibilidad

Después de la actualización, verifica que:

1. ✅ Compilación exitosa: `./gradlew clean build`
2. ✅ Tests pasen: `./gradlew test`
3. ✅ Aplicación arranque: `./gradlew bootRun`
4. ✅ JWT se genere y valide correctamente
5. ✅ Spring Security funcione con nuevas rutas
6. ✅ CORS funcione desde el frontend

## Cambios no realizados (mejoras futuras)

Estos cambios podrían implementarse en futuras iteraciones:

1. **Mejorar configuración de CORS**: Leer `allowed-origins` desde `application.properties`
2. **Remover deprecations de SignatureAlgorithm**: Usar API más moderna
3. **Agregar Micrometer Observability**: Para tracing distribuido
4. **Compilación a native image**: Crear binario ejecutable con GraalVM
5. **Mejorar seguridad del JWT en frontend**: Usar cookies httpOnly en lugar de localStorage

## Referencia de actualizaciones

- Spring Boot 3.3: https://spring.io/projects/spring-boot#learn
- Spring Security 6: https://spring.io/projects/spring-security
- JJWT 0.12: https://github.com/jwtk/jjwt
- Jakarta EE 10: https://jakarta.ee/

## Contacto y soporte

Si encuentras problemas con esta migración, revisa:
- Los logs de la aplicación
- La salida del compilador Gradle
- Las pruebas unitarias en `src/test/java`


