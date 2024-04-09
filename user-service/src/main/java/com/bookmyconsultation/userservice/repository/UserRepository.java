package com.bookmyconsultation.userservice.repository;

import com.bookmyconsultation.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
