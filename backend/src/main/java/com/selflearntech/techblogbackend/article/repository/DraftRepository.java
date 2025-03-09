package com.selflearntech.techblogbackend.article.repository;

import com.selflearntech.techblogbackend.article.model.Draft;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DraftRepository extends MongoRepository<Draft, String> {

    Optional<Draft> findById(String id);
}
