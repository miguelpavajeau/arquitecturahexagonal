package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

import java.util.List;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public void saveUser(UserModel userModel) {
        //Obtener usuario en base de datos donde el correo sea igual al del UserModel
        //Si el usuario ya existe, entonces arroja una excepción UserAlreadyExistsException 400
        //Si el usuario no existe, entonces guarda el usuario en la base de datos
        //Obtener usuario en base de datos donde el correo sea igual al del UserModel
        //Cada uno de los campo son obligatorios
        if (userModel.getNombre() == null || userModel.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (userModel.getApellido() == null || userModel.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (userModel.getCorreo() == null || userModel.getCorreo().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        userPersistencePort.saveUser(userModel);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userPersistencePort.getAllUsers();
    }

//    @Override
//    public String getUserByEmail(String correo) {
//        return null;
//    }
}