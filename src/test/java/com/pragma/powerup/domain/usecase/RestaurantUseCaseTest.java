package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RestaurantUseCaseTest {

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    IUserPersistencePort userPersistencePort;

    @InjectMocks
    RestaurantUseCase restaurantUseCase;

    private RestaurantModel buildRestaurant() {
        return new RestaurantModel(null, "Mi Restaurante", 900123456L, "Calle 1 # 2-3",
                "+573001234567", "http://logo.png", 1L);
    }

    @Test
    void should_Save_Restaurant_When_Owner_Has_Propietario_Role() {
        RestaurantModel restaurantModel = buildRestaurant();
        RoleModel roleModel = new RoleModel(1L, "PROPIETARIO", "Propietario de un restaurante");
        UserModel userModel = new UserModel(1L, "Juan", "Perez", 123456789L, "3001234567",
                "juan@mail.com", "clave", roleModel);

        when(userPersistencePort.findById(1L)).thenReturn(userModel);
        when(restaurantPersistencePort.saveRestaurant(restaurantModel)).thenReturn(restaurantModel);

        restaurantUseCase.saveRestaurant(restaurantModel);

        verify(restaurantPersistencePort).saveRestaurant(restaurantModel);
    }

    @Test
    void should_Throw_When_Owner_Does_Not_Exist() {
        RestaurantModel restaurantModel = buildRestaurant();

        when(userPersistencePort.findById(1L)).thenReturn(null);

        assertThrows(DomainException.class, () -> restaurantUseCase.saveRestaurant(restaurantModel));
    }

    @Test
    void should_Throw_When_Owner_Role_Is_Not_Propietario() {
        RestaurantModel restaurantModel = buildRestaurant();
        RoleModel roleModel = new RoleModel(2L, "CLIENTE", "Cliente de la plazoleta de comidas");
        UserModel userModel = new UserModel(1L, "Juan", "Perez", 123456789L, "3001234567",
                "juan@mail.com", "clave", roleModel);

        when(userPersistencePort.findById(1L)).thenReturn(userModel);

        assertThrows(DomainException.class, () -> restaurantUseCase.saveRestaurant(restaurantModel));
    }

    @Test
    void should_Throw_When_Nombre_Is_Only_Numbers() {
        RestaurantModel restaurantModel = buildRestaurant();
        restaurantModel.setNombre("12345");

        assertThrows(DomainException.class, () -> restaurantUseCase.saveRestaurant(restaurantModel));
    }
}
