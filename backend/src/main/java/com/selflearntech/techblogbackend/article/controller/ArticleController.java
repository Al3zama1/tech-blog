package com.selflearntech.techblogbackend.article.controller;

import com.selflearntech.techblogbackend.article.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ArticleController {

    private final IArticleService articleService;

    @PostMapping("/create-draft")
    public ResponseEntity<Void> createDraft() {
        int draftId = articleService.createDraft();
        return ResponseEntity.created(URI.create("/api/v1/drafts/" + draftId)).build();
    }
}
