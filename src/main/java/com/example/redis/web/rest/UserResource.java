package com.example.redis.web.rest;

import com.example.redis.domain.service.UserService;
import com.example.redis.infrastructure.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserResource {

    @Autowired
    private UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/user/{id}")
    @Cacheable(value = "users",key = "#id",unless = "#result.id<3")
    public Optional<User> getUser(@PathVariable long id){
        return userService.findById(id);
    }

    @PostMapping("/user")
    public User create(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("/users")
    public Page<User> getAll(Pageable pageable){
        return userService.findAll(pageable);
    }

    @PutMapping("/user")
     @CachePut(value = "users",key = "#user.id")//to update cache when data updated
    public User updateUser(@RequestBody User user)
    {
        return userService.update(user);
    }

    @DeleteMapping("/user/{id}")
//     @CacheEvict(value = "users",allEntries = false,key = "#id")//when user is deleted then also user exists in cache, to avoid thi we add this annotation
    public boolean deleteUser(@PathVariable Long id)
    {
        return userService.deleteById(id);
    }
}
