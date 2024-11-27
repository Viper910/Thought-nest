package io.microservices.authentication.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import io.microservices.authentication.model.User;

public interface UserRepository extends MongoRepository<String, User> {
}