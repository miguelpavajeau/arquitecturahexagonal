package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.spi.IRolePersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RoleUseCaseTest {
    @Mock
    IRolePersistencePort rolePersistencePort;

    @InjectMocks
    RoleUseCase roleUseCase;

    @Test
    void should_Save_User() {
        //given
        RoleModel roleModel = new RoleModel();

        //when
        when(rolePersistencePort.saveRole(roleModel)).thenReturn(roleModel);
        roleUseCase.saveRole(roleModel);

        //then
        verify(rolePersistencePort).saveRole(roleModel);
    }

    @Test
    void should_Get_All_Roles() {
        //given
        RoleModel roleModel = new RoleModel();

        //when
        when(rolePersistencePort.getAllRoles()).thenReturn(List.of(roleModel));
        roleUseCase.getAllRoles();

        //then
        verify(rolePersistencePort).getAllRoles();
    }
}