package com.i9developed.oauth2.ws.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.i9developed.oauth2.ws.domain.User;
import com.i9developed.oauth2.ws.domain.VerificationToken;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
	
	Optional<VerificationToken> findByToken(String token);
	Optional<VerificationToken> findByUser(User user);

}
