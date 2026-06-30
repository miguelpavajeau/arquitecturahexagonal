package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.AuthenticationFailedException;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.util.IJwtGenerator;
import com.pragma.powerup.domain.util.IUserPasswordEncrypt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AuthenticationUseCaseTest {

    @Mock
    IUserPersistencePort userPersistencePort;

    @Mock
    IUserPasswordEncrypt userPasswordEncrypt;

    @Mock
    IJwtGenerator jwtGenerator;

    @InjectMocks
    AuthenticationUseCase authenticationUseCase;

    @Test
    void should_Login_And_Return_Token() {
        RoleModel roleModel = new RoleModel(1L, "ADMINISTRADOR", "Administrador de la plataforma");
        UserModel userModel = new UserModel(1L, "Admin", "Pragma", 123456789L, "3001234567",
                "admin@pragma.com", "encriptada", roleModel);

        when(userPersistencePort.findByCorreo("admin@pragma.com")).thenReturn(userModel);
        when(userPasswordEncrypt.checkPassword("Admin123*", "encriptada")).thenReturn(true);
        when(jwtGenerator.generateToken(userModel)).thenReturn("token-jwt");

        String token = authenticationUseCase.login("admin@pragma.com", "Admin123*");

        assertEquals("token-jwt", token);
    }

    @Test
    void should_Throw_When_User_Not_Found() {
        when(userPersistencePort.findByCorreo("noexiste@mail.com")).thenReturn(null);

        assertThrows(AuthenticationFailedException.class,
                () -> authenticationUseCase.login("noexiste@mail.com", "clave"));
    }

    @Test
    void should_Throw_When_Password_Does_Not_Match() {
        RoleModel roleModel = new RoleModel(1L, "PROPIETARIO", "Propietario de un restaurante");
        UserModel userModel = new UserModel(1L, "Juan", "Perez", 123456789L, "3001234567",
                "juan@mail.com", "encriptada", roleModel);

        when(userPersistencePort.findByCorreo("juan@mail.com")).thenReturn(userModel);
        when(userPasswordEncrypt.checkPassword("malaClave", "encriptada")).thenReturn(false);

        assertThrows(AuthenticationFailedException.class,
                () -> authenticationUseCase.login("juan@mail.com", "malaClave"));
    }
}
