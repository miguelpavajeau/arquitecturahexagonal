package com.pragma.powerup.domain.api;

public interface IAuthenticationServicePort {

    String login(String correo, String clave);
}
