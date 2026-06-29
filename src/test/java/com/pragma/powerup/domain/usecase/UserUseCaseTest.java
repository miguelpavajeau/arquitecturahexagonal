package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.CorreoYaExisteException;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IRolePersistencePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserUseCaseTest {

    @Mock
    IUserPersistencePort userPersistencePort;

    @Mock
    IRolePersistencePort rolePersistencePort;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    @InjectMocks
    UserUseCase userUseCase;

    @Test
    void should_Save_User() {
        UserModel userModel = new UserModel(null, "Juan", "Perez", 123456789L, "3001234567", "juan@mail.com", "clave", null);
        RoleModel roleModel = new RoleModel(1L, "PROPIETARIO", "Propietario de un restaurante");

        when(rolePersistencePort.findRoleByName("PROPIETARIO")).thenReturn(roleModel);
        when(userPersistencePort.saveUser(userModel)).thenReturn(userModel);
        userUseCase.saveUser(userModel);

        verify(userPersistencePort).saveUser(userModel);
    }

    @Test
    void should_Throw_When_Nombre_Is_Null() {
        UserModel userModel = new UserModel(null, null, "Perez", 123456789L, "3001234567", "juan@mail.com", "clave", null);

        assertThrows(DomainException.class, () -> userUseCase.saveUser(userModel));
    }

    @Test
    void should_Throw_When_Apellido_Is_Null() {
        UserModel userModel = new UserModel(null, "Juan", null, 123456789L, "3001234567", "juan@mail.com", "clave", null);

        assertThrows(DomainException.class, () -> userUseCase.saveUser(userModel));
    }

    @Test
    void should_Throw_When_Correo_Is_Null() {
        UserModel userModel = new UserModel(null, "Juan", "Perez", 123456789L, "3001234567", null, "clave", null);

        assertThrows(DomainException.class, () -> userUseCase.saveUser(userModel));
    }

    @Test
    void should_Throw_When_Correo_Already_Exists() {
        UserModel userModel = new UserModel(null, "Juan", "Perez", 123456789L, "3001234567", "juan@mail.com", "clave", null);

        when(userPersistencePort.existsByCorreo("juan@mail.com")).thenReturn(true);

        assertThrows(CorreoYaExisteException.class, () -> userUseCase.saveUser(userModel));
    }

    @Test
    void should_Throw_When_Role_Propietario_Not_Configured() {
        UserModel userModel = new UserModel(null, "Juan", "Perez", 123456789L, "3001234567", "juan@mail.com", "clave", null);

        when(rolePersistencePort.findRoleByName("PROPIETARIO")).thenReturn(null);

        assertThrows(DomainException.class, () -> userUseCase.saveUser(userModel));
    }

    @Test
    void should_Get_All_Users() {
        UserModel userModel = new UserModel(1L, "Juan", "Perez", 123456789L, "3001234567", "juan@mail.com", "clave", null);

        when(userPersistencePort.getAllUsers()).thenReturn(List.of(userModel));
        userUseCase.getAllUsers();

        verify(userPersistencePort).getAllUsers();
    }
}
