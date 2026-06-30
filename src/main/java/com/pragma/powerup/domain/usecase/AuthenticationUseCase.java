package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IAuthenticationServicePort;
import com.pragma.powerup.domain.exception.AuthenticationFailedException;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.util.IJwtGenerator;
import com.pragma.powerup.domain.util.IUserPasswordEncrypt;

public class AuthenticationUseCase implements IAuthenticationServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IUserPasswordEncrypt userPasswordEncrypt;
    private final IJwtGenerator jwtGenerator;

    public AuthenticationUseCase(IUserPersistencePort userPersistencePort, IUserPasswordEncrypt userPasswordEncrypt,
                                  IJwtGenerator jwtGenerator) {
        this.userPersistencePort = userPersistencePort;
        this.userPasswordEncrypt = userPasswordEncrypt;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public String login(String correo, String clave) {
        UserModel userModel = userPersistencePort.findByCorreo(correo);
        if (userModel == null || !userPasswordEncrypt.checkPassword(clave, userModel.getClave())) {
            throw new AuthenticationFailedException();
        }
        return jwtGenerator.generateToken(userModel);
    }
}
