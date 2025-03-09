package com.selflearntech.techblogbackend.article.controller;

import com.selflearntech.techblogbackend.article.dto.DraftDTO;
import com.selflearntech.techblogbackend.article.service.IArticleService;
import com.selflearntech.techblogbackend.article.service.IDraftService;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.results.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/drafts")
@RequiredArgsConstructor
public class DraftController {

    private final IDraftService draftService;

    @PostMapping
    public ResponseEntity<Void> createDraft(JwtAuthenticationToken authentication) {
        String draftId = draftService.createDraft(authentication.getName());

        return ResponseEntity
                .created(URI.create("/api/v1/draft/" + draftId))
                .build();
    }

    @PostMapping("/{draftId}")
    public ResponseEntity<Map<String, String>> publishDraft(@NotBlank @PathVariable String draftId, JwtAuthenticationToken authentication) {
        String articleSlug = draftService.publishDraft(authentication.getName(), draftId);

        return ResponseEntity.created(URI.create("/api/v1/articles/" + articleSlug))
                .body(Map.of("slug", articleSlug));
    }

    @GetMapping("/{draftId}")
    public DraftDTO getDraft(@NotBlank @PathVariable String draftId) {
        return draftService.getDraft(draftId);
    }

    @PutMapping("/{draftId}")
    public void editDraft(@NotBlank @PathVariable String draftId, @Valid @RequestBody DraftDTO payload, JwtAuthenticationToken authentication) {
        draftService.editDraft(draftId, authentication.getName(), payload);
    }
}
