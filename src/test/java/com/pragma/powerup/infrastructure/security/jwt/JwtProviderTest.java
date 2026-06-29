package com.pragma.powerup.infrastructure.security.jwt;

import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtProviderTest {

    private static final String SECRET = "EstaEsUnaClaveSecretaDePruebasParaJwtProviderTest";

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(SECRET, 60_000L);
    }

    @Test
    void should_Generate_And_Validate_Token() {
        RoleModel roleModel = new RoleModel(1L, "ADMINISTRADOR", "Administrador de la plataforma");
        UserModel userModel = new UserModel(1L, "Admin", "Pragma", 123456789L, "3001234567",
                "admin@pragma.com", "clave", roleModel);

        String token = jwtProvider.generateToken(userModel);

        assertTrue(jwtProvider.validateToken(token));
        assertEquals("admin@pragma.com", jwtProvider.getCorreoFromToken(token));
        assertEquals("ADMINISTRADOR", jwtProvider.getRoleFromToken(token));
    }

    @Test
    void should_Not_Validate_Tampered_Token() {
        RoleModel roleModel = new RoleModel(1L, "PROPIETARIO", "Propietario de un restaurante");
        UserModel userModel = new UserModel(1L, "Juan", "Perez", 123456789L, "3001234567",
                "juan@mail.com", "clave", roleModel);

        String token = jwtProvider.generateToken(userModel) + "tampered";

        assertFalse(jwtProvider.validateToken(token));
    }

    @Test
    void should_Not_Validate_Token_Signed_With_Different_Secret() {
        JwtProvider otherProvider = new JwtProvider("OtraClaveSecretaTotalmenteDistintaParaElTest", 60_000L);
        RoleModel roleModel = new RoleModel(1L, "CLIENTE", "Cliente de la plazoleta de comidas");
        UserModel userModel = new UserModel(1L, "Ana", "Gomez", 987654321L, "3007654321",
                "ana@mail.com", "clave", roleModel);

        String token = otherProvider.generateToken(userModel);

        assertFalse(jwtProvider.validateToken(token));
    }
}
