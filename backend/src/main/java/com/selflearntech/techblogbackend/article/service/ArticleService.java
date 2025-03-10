package com.selflearntech.techblogbackend.article.service;

import com.selflearntech.techblogbackend.article.dto.ArticleDTO;
import com.selflearntech.techblogbackend.article.dto.ArticlePreviewEntryDTO;
import com.selflearntech.techblogbackend.article.dto.ArticlePreviewPageDTO;
import com.selflearntech.techblogbackend.article.dto.AuthorDTO;
import com.selflearntech.techblogbackend.article.mapper.ArticleMapper;
import com.selflearntech.techblogbackend.article.mapper.AuthorMapper;
import com.selflearntech.techblogbackend.article.model.Article;
import com.selflearntech.techblogbackend.article.repository.ArticleRepository;
import com.selflearntech.techblogbackend.article.repository.DraftRepository;
import com.selflearntech.techblogbackend.exception.ErrorMessages;
import com.selflearntech.techblogbackend.exception.NotFoundException;
import com.selflearntech.techblogbackend.user.model.User;
import com.selflearntech.techblogbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService implements IArticleService{

    private final Clock clock;
    private final ArticleMapper articleMapper;
    private final AuthorMapper authorMapper;
    private final ArticleRepository articleRepository;
    private final DraftRepository draftRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public ArticleDTO getArticle(String articleSlug) {
        Article article = articleRepository.findBySlug(articleSlug).orElseThrow(() -> new RuntimeException("Article does not exist"));

        AuthorDTO author = authorMapper.toAuthorDTO(article.getUser());
        return articleMapper.toArticleDTO(article, author);
    }

    @Override
    public ArticlePreviewPageDTO getFeaturedArticles(int page, int limit, String sortQuery) {
        Query query = Query.query(Criteria.where("isFeatured").is(true));
        query.with(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt")));

        List<Article> articles = mongoTemplate.find(query, Article.class);
        long totalCount = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Article.class);
        boolean hasMorePages = ((page + 1) * limit) < totalCount;

        List<ArticlePreviewEntryDTO> articlesDto = articles.stream().map(article -> articleMapper
                .toArticlePreviewDTO(article, authorMapper.toAuthorDTO(article.getUser()))).toList();

        return new ArticlePreviewPageDTO(articlesDto, hasMorePages);
    }

    @Override
    public ArticlePreviewPageDTO getArticles(int page, int limit, String category, String sortQuery) {
        Query query = new Query();

        if (!category.isEmpty()) {
            category = category.replace("-", " ");
            Criteria categoryCriteria = Criteria.where("category").is(category);
            query.addCriteria(categoryCriteria);
        }

        // TODO: for testing, right now is being set to newest by default. Later alter to account for other sorting methods (popularity, newest, trending)
        query.with(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt")));

        List<Article> articles = mongoTemplate.find(query, Article.class);
        long totalCount = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Article.class);
        boolean hasMorePages = ((page + 1) * limit) < totalCount;


        List<ArticlePreviewEntryDTO> articlesDto = articles.stream().map(article -> {
            AuthorDTO authorDTO = authorMapper.toAuthorDTO(article.getUser());
            return articleMapper.toArticlePreviewDTO(article, authorDTO);
        }).toList();

        return new ArticlePreviewPageDTO(articlesDto, hasMorePages);
    }


    @Override
    public void deleteArticle(String slug) {
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article does not exist"));
        articleRepository.delete(article);
    }

    @Override
    public void featureArticle(String slug) {
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));

        article.setFeatured(true);
    }

    @Override
    public void bookMarkArticle(String slug, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));

        user.getSavedArticles().add(article);
    }

}
