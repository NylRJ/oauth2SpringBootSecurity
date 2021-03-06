package com.i9developed.oauth2.ws.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.i9developed.oauth2.ws.domain.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String>{
	
	
	Optional<Role> findByName(String name);

}
