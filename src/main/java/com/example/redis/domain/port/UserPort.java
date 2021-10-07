package com.example.redis.domain.port;

import com.example.redis.infrastructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserPort {

    User createUser(User user);

    Optional<User> findById(Long id);
    Page<User> findAll(Pageable pageable);

    User update(User user);

    boolean deleteById(Long id);

}
