package com.example.redis.infrastructure.adaptor;

import com.example.redis.domain.port.UserPort;
import com.example.redis.infrastructure.entity.User;
import com.example.redis.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserJPAAdapter implements UserPort {

    @Autowired
    UserRepository userRepository;


    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
//    @Cacheable(value = "users",key = "#id",unless = "#result.id<3")
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
//    @CachePut(value = "users",key = "#user.id")//to update cache when data updated
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    //@CacheEvict(value = "users",allEntries = false,key = "#id")//when user is deleted then also user exists in cache, to avoid this we add this annotation
    public boolean deleteById(Long id) {
         userRepository.deleteById(id);
         return true;
    }
}
