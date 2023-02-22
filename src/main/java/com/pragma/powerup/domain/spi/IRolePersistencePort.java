package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;

import java.util.List;

public interface IRolePersistencePort {
    RoleModel saveRole(RoleModel roleModel);

    List<RoleModel> getAllRoles();
}