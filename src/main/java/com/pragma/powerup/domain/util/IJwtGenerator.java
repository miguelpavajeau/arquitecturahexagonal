package com.pragma.powerup.domain.util;

import com.pragma.powerup.domain.model.UserModel;

public interface IJwtGenerator {

    String generateToken(UserModel userModel);
}
