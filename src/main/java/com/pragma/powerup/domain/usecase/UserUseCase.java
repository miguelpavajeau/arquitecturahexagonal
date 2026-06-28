package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.exception.CorreoYaExisteException;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.EmployeeRestaurantModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IRolePersistencePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

import java.util.List;

public class UserUseCase implements IUserServicePort {

    private static final String ROL_PROPIETARIO = "PROPIETARIO";
    private static final String ROL_EMPLEADO = "EMPLEADO";
    private static final String ROL_CLIENTE = "CLIENTE";

    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort, IRolePersistencePort rolePersistencePort,
                       IRestaurantPersistencePort restaurantPersistencePort,
                       IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort) {
        this.userPersistencePort = userPersistencePort;
        this.rolePersistencePort = rolePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
    }

    @Override
    public void saveUser(UserModel userModel) {
        assignRoleAndSave(userModel, ROL_PROPIETARIO);
    }

    @Override
    public void saveEmployee(UserModel userModel, String correoPropietario) {
        RestaurantModel restaurant = resolveOwnerRestaurant(correoPropietario);
        UserModel savedEmployee = assignRoleAndSave(userModel, ROL_EMPLEADO);
        employeeRestaurantPersistencePort.save(
                new EmployeeRestaurantModel(null, savedEmployee.getId(), restaurant.getId()));
    }

    @Override
    public void saveClient(UserModel userModel) {
        assignRoleAndSave(userModel, ROL_CLIENTE);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userPersistencePort.getAllUsers();
    }

    private RestaurantModel resolveOwnerRestaurant(String correoPropietario) {
        UserModel propietario = userPersistencePort.findByCorreo(correoPropietario);
        if (propietario == null) {
            throw new DomainException("No se pudo identificar al propietario autenticado");
        }
        RestaurantModel restaurant = restaurantPersistencePort.findByPropietarioId(propietario.getId());
        if (restaurant == null) {
            throw new DomainException("El propietario no tiene un restaurante registrado");
        }
        return restaurant;
    }

    private UserModel assignRoleAndSave(UserModel userModel, String roleName) {
        if (userModel.getNombre() == null || userModel.getNombre().isBlank()) {
            throw new DomainException("El nombre es obligatorio");
        }
        if (userModel.getApellido() == null || userModel.getApellido().isBlank()) {
            throw new DomainException("El apellido es obligatorio");
        }
        if (userModel.getCorreo() == null || userModel.getCorreo().isBlank()) {
            throw new DomainException("El correo es obligatorio");
        }
        if (userPersistencePort.existsByCorreo(userModel.getCorreo())) {
            throw new CorreoYaExisteException();
        }
        RoleModel roleModel = rolePersistencePort.findRoleByName(roleName);
        if (roleModel == null) {
            throw new DomainException("El rol " + roleName + " no esta configurado en el sistema");
        }
        userModel.setRole(roleModel);
        return userPersistencePort.saveUser(userModel);
    }
}
