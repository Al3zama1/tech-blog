package com.selflearntech.techblogbackend.article.service;


import com.selflearntech.techblogbackend.article.dto.ArticleDTO;
import com.selflearntech.techblogbackend.article.dto.ArticlesPageDTO;

public interface IArticleService {

    ArticleDTO getArticle(String articleSlug);
    ArticlesPageDTO getFeaturedArticles(int page, int limit, String sortQuery);
    ArticlesPageDTO getArticles(int page, int limit, String category, String sortQuery);
    void deleteArticle(String slug);
    void featureArticle(String slug);
    void bookMarkArticle(String slug, String userEmail);

}
