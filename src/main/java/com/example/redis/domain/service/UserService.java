package com.example.redis.domain.service;

import com.example.redis.domain.port.UserPort;
import com.example.redis.infrastructure.entity.User;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {


    private final UserPort userPort;

    @Autowired
    private RMapCache<Long, User> userRMapCache;

    public UserService(UserPort userPort) {
        this.userPort = userPort;
    }

    public User createUser(final User user){
        long random=(long) (Math.random()*(7000-4000+1)+4000);
        this.userRMapCache.put(random, user,60, TimeUnit.SECONDS);
        //return userPort.createUser(user);
        return user;
    }

    public Optional<User> findById(Long id){
        return Optional.ofNullable(this.userRMapCache.get(id));
        //return  userPort.findById(id);
    }

    public Page<User> findAll(Pageable pageable){
        return  userPort.findAll(pageable);
    }

    public User update(User user){
        return this.userRMapCache.put(user.getId(),user);
        //return userPort.update(user);

    }

    public boolean deleteById(Long id){
        this.userRMapCache.remove(id);
        return true;
        //return  userPort.deleteById(id);
    }
}
