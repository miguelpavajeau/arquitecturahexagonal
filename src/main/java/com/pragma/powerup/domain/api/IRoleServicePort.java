package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.RoleModel;

import java.util.List;

public interface IRoleServicePort {

    void saveRole(RoleModel roleModel);

    List<RoleModel> getAllRoles();

}