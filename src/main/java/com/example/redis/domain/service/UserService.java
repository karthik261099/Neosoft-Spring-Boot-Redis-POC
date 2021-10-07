package com.example.redis.domain.service;

import com.example.redis.domain.port.UserPort;
import com.example.redis.infrastructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {


    private final UserPort userPort;


    public UserService(UserPort userPort) {
        this.userPort = userPort;
    }

    public User createUser(User user){
        return userPort.createUser(user);
    }

    public Optional<User> findById(Long id){
        return  userPort.findById(id);
    }

    public Page<User> findAll(Pageable pageable){
        return  userPort.findAll(pageable);
    }

    public User update(User user){
        return userPort.update(user);
    }

    public boolean deleteById(Long id){
        return  userPort.deleteById(id);
    }
}
