package com.i9developed.oauth2.ws.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.i9developed.oauth2.ws.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	
	
	Optional<User> findByEmail(String email);

}
