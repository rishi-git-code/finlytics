package com.finlytics.repository;

import com.finlytics.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    //User findByEmail(String email);
    Optional<User> findByEmail(String email);
}
