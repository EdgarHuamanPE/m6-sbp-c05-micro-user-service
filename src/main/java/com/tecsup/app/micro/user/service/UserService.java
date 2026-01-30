package com.tecsup.app.micro.user.service;

import com.tecsup.app.micro.user.dto.User;
import com.tecsup.app.micro.user.entity.UserEntity;
import com.tecsup.app.micro.user.mapper.UserMapper;
import com.tecsup.app.micro.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


//@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getUserById(Long id){
       UserEntity entity = userRepository.findById(id).orElse(null);
        return  userMapper.toDomain(entity);
    }


    public List<User> getAllUsers() {
       List<UserEntity> users =userRepository.findAll();
       return  this.userMapper.toDomain(users);
    }
}
