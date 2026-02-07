package com.tecsup.app.micro.user.service;

import com.tecsup.app.micro.user.dto.User;
import com.tecsup.app.micro.user.entity.UserEntity;
import com.tecsup.app.micro.user.exception.DuplicateEmailException;
import com.tecsup.app.micro.user.exception.NotFoundUser;
import com.tecsup.app.micro.user.mapper.UserMapper;
import com.tecsup.app.micro.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return  userRepository.findById(id)
                .map(this.userMapper::toDomain)
                .orElseThrow(()->new NotFoundUser(id));
    }

    public List<User> getAllUsers() {
       List<UserEntity> users =userRepository.findAll();
       return  this.userMapper.toDomainLista(users);
    }

    public User saveUser(User user) {

        UserEntity userEntity = userMapper.toEntity(user);
        if (userRepository.existsByEmail(userEntity.getEmail())) {
            throw new DuplicateEmailException(userEntity.getEmail());
        }
        return userMapper.toDomain(userRepository.save(userEntity));
    }

    public void deleteUser(User user) {
        userRepository.delete(userMapper.toEntity(user));
    }

}
