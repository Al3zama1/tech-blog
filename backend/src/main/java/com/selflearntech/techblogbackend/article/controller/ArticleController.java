package com.selflearntech.techblogbackend.article.controller;

import com.selflearntech.techblogbackend.article.dto.ArticleDTO;
import com.selflearntech.techblogbackend.article.dto.ArticlesPageDTO;
import com.selflearntech.techblogbackend.article.service.IArticleService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Validated
public class ArticleController {

    private final IArticleService articleService;


    @GetMapping
    public ArticlesPageDTO getArticles(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "cat", defaultValue = "") String category,
            @RequestParam(value = "sort", defaultValue = "newest") String sortQuery,
            @RequestParam(value = "featured", defaultValue = "false") boolean featured) {

        if (featured) return articleService.getFeaturedArticles(page, limit, sortQuery);
        return articleService.getArticles(page, limit, category, sortQuery);
    }

    @GetMapping("/{slug}")
    public ArticleDTO getArticle(@PathVariable String slug) {
        return articleService.getArticle(slug);
    }

    @PatchMapping("/{slug/feature}")
    public void featureArticle(@NotBlank @PathVariable String slug) {
        System.out.println(slug + " featured");
    }

    @DeleteMapping("/{slug}")
    public void deleteArticle(@PathVariable String slug) {
        System.out.println(slug + " deleted");
    }
}
