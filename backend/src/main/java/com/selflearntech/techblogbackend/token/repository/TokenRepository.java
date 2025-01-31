package com.selflearntech.techblogbackend.token.repository;

import com.selflearntech.techblogbackend.token.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

}
