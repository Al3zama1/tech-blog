package com.selflearntech.techblogbackend.article.repository;

import com.selflearntech.techblogbackend.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
}

