package com.pragma.powerup.infrastructure;

import com.pragma.powerup.domain.util.IUserPasswordEncrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserPasswordEncryptImpl implements IUserPasswordEncrypt {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encryptPassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean checkPassword(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }
}
