package io.microservices.authentication.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.microservices.authentication.model.User;
import java.util.List;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public Optional<List<User>> findByUsernameOrEmail(String username, String email);
}