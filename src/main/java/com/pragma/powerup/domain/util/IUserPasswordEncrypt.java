package com.pragma.powerup.domain.util;

public interface IUserPasswordEncrypt {

    String encryptPassword(String password);

    boolean checkPassword(String password, String encryptedPassword);
}