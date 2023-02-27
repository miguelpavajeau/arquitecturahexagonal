package com.pragma.powerup.infrastructure;

import com.pragma.powerup.domain.util.IUserPasswordEncrypt;

public class UserPasswordEncryptImpl implements IUserPasswordEncrypt {

    @Override
    public String encryptPassword(String password) {
        return null;
    }

    @Override
    public boolean checkPassword(String password, String encryptedPassword) {
        return false;
    }
}