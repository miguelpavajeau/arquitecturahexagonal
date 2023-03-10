package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserUseCaseTest {
    @Mock
    IUserPersistencePort userPersistencePort;

    @InjectMocks
    UserUseCase userUseCase;

    @Test
    void should_Save_User() {
        //given
        UserModel userModel = new UserModel();

        //when
        when(userPersistencePort.saveUser(userModel)).thenReturn(userModel);
        userUseCase.saveUser(userModel);

        //then
        verify(userPersistencePort).saveUser(userModel);
    }

    @Test
    void should_Get_All_Users() {
        //given
        UserModel userModel = new UserModel();

        //when
        when(userPersistencePort.getAllUsers()).thenReturn(List.of(userModel));
        userUseCase.getAllUsers();

        //then
        verify(userPersistencePort).getAllUsers();
    }
}