package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.exception.CorreoYaExisteException;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public UserModel saveUser(UserModel userModel) {
        try {
            UserEntity userEntity = userRepository.save(userEntityMapper.toEntity(userModel));
            return userEntityMapper.toUserModel(userEntity);
        } catch (DataIntegrityViolationException e) {
            // Última línea de defensa ante inserciones concurrentes con el mismo correo:
            // la regla de negocio se valida en el dominio, pero la restricción de unicidad
            // de BD cubre la condición de carrera.
            throw new CorreoYaExisteException();
        }
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return userRepository.findByCorreo(correo) != null;
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<UserEntity> entityList = userRepository.findAll();
        if (entityList.isEmpty()) {
            return Collections.emptyList();
        }
        return userEntityMapper.toUserModelList(entityList);
    }

    @Override
    public UserModel findByCorreo(String correo) {
        UserEntity userEntity = userRepository.findByCorreo(correo);
        return userEntity == null ? null : userEntityMapper.toUserModel(userEntity);
    }

    @Override
    public UserModel findById(Long id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toUserModel)
                .orElse(null);
    }
}
