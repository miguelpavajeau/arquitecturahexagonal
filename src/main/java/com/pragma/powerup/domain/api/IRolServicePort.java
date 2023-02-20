package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.UserModel;

import java.util.List;

public interface IRolServicePort {

    void saveUser(UserModel userModel);

    List<UserModel> getAllUsers();

}