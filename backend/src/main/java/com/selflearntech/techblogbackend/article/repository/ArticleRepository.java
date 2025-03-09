package com.selflearntech.techblogbackend.article.repository;

import com.selflearntech.techblogbackend.article.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

    Optional<Article> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
