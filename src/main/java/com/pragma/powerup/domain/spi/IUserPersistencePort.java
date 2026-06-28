package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.UserModel;

import java.util.List;

public interface IUserPersistencePort {
    UserModel saveUser(UserModel userModel);

    boolean existsByCorreo(String correo);

    List<UserModel> getAllUsers();

    UserModel findByCorreo(String correo);

    UserModel findById(Long id);
}
