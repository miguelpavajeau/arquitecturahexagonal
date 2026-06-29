package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.exception.EntityNotFoundException;
import com.pragma.powerup.domain.model.CategoryModel;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DishUseCaseTest {

    @Mock
    IDishPersistencePort dishPersistencePort;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    IUserPersistencePort userPersistencePort;

    @InjectMocks
    DishUseCase dishUseCase;

    private static final String OWNER_CORREO = "owner@mail.com";
    private static final String OTHER_CORREO = "other@mail.com";

    private DishModel buildDish() {
        CategoryModel categoryModel = new CategoryModel(1L, "Comida rapida", "Categoria de comida rapida");
        return new DishModel(null, "Hamburguesa", 15000, "Hamburguesa con queso", "http://img.png",
                10L, categoryModel, null);
    }

    private RestaurantModel buildRestaurant() {
        return new RestaurantModel(10L, "Mi Restaurante", 900123456L, "Calle 1", "+573001234567",
                "http://logo.png", 5L);
    }

    @Test
    void should_Save_Dish_When_Authenticated_User_Is_Owner() {
        DishModel dishModel = buildDish();
        RestaurantModel restaurantModel = buildRestaurant();
        UserModel ownerModel = new UserModel(5L, "Juan", "Perez", 123456789L, "3001234567", OWNER_CORREO, "clave", null);

        when(restaurantPersistencePort.findById(10L)).thenReturn(restaurantModel);
        when(userPersistencePort.findByCorreo(OWNER_CORREO)).thenReturn(ownerModel);
        when(dishPersistencePort.saveDish(dishModel)).thenReturn(dishModel);

        dishUseCase.saveDish(dishModel, OWNER_CORREO);

        verify(dishPersistencePort).saveDish(dishModel);
        assertEquals(Boolean.TRUE, dishModel.getActivo());
    }

    @Test
    void should_Throw_When_Restaurant_Does_Not_Exist() {
        DishModel dishModel = buildDish();

        when(restaurantPersistencePort.findById(10L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> dishUseCase.saveDish(dishModel, OWNER_CORREO));
    }

    @Test
    void should_Throw_When_Authenticated_User_Is_Not_Owner() {
        DishModel dishModel = buildDish();
        RestaurantModel restaurantModel = buildRestaurant();
        UserModel otherUser = new UserModel(99L, "Otro", "Propietario", 987654321L, "3009999999", OTHER_CORREO, "clave", null);

        when(restaurantPersistencePort.findById(10L)).thenReturn(restaurantModel);
        when(userPersistencePort.findByCorreo(OTHER_CORREO)).thenReturn(otherUser);

        assertThrows(DomainException.class, () -> dishUseCase.saveDish(dishModel, OTHER_CORREO));
    }

    @Test
    void should_Update_Dish_When_Authenticated_User_Is_Owner() {
        DishModel existingDish = buildDish();
        existingDish.setId(50L);
        RestaurantModel restaurantModel = buildRestaurant();
        UserModel ownerModel = new UserModel(5L, "Juan", "Perez", 123456789L, "3001234567", OWNER_CORREO, "clave", null);

        when(dishPersistencePort.findById(50L)).thenReturn(existingDish);
        when(restaurantPersistencePort.findById(10L)).thenReturn(restaurantModel);
        when(userPersistencePort.findByCorreo(OWNER_CORREO)).thenReturn(ownerModel);
        when(dishPersistencePort.updateDish(existingDish)).thenReturn(existingDish);

        dishUseCase.updateDish(50L, 20000, "Nueva descripcion", OWNER_CORREO);

        verify(dishPersistencePort).updateDish(existingDish);
        assertEquals(20000, existingDish.getPrecio());
        assertEquals("Nueva descripcion", existingDish.getDescripcion());
    }

    @Test
    void should_Throw_When_Updating_Dish_As_Non_Owner() {
        DishModel existingDish = buildDish();
        existingDish.setId(50L);
        RestaurantModel restaurantModel = buildRestaurant();
        UserModel otherUser = new UserModel(99L, "Otro", "Propietario", 987654321L, "3009999999", OTHER_CORREO, "clave", null);

        when(dishPersistencePort.findById(50L)).thenReturn(existingDish);
        when(restaurantPersistencePort.findById(10L)).thenReturn(restaurantModel);
        when(userPersistencePort.findByCorreo(OTHER_CORREO)).thenReturn(otherUser);

        assertThrows(DomainException.class,
                () -> dishUseCase.updateDish(50L, 20000, "Nueva descripcion", OTHER_CORREO));
    }
}
