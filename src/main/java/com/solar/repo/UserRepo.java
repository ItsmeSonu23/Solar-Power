package com.solar.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.solar.entity.User;
@Repository
public interface UserRepo extends MongoRepository<User,String>{
    Optional<User> findByUserEmail(String userEmail);
}
