package com.selflearntech.techblogbackend.article.controller;

import com.selflearntech.techblogbackend.article.dto.CreateDraftRequestDTO;
import com.selflearntech.techblogbackend.article.dto.DraftDTO;
import com.selflearntech.techblogbackend.article.dto.PublishDraftRequestDTO;
import com.selflearntech.techblogbackend.article.service.IDraftService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/drafts")
@RequiredArgsConstructor
@Validated
public class DraftController {

    private final IDraftService draftService;

    @PostMapping
    public ResponseEntity<Void> createDraft(@Valid @RequestBody CreateDraftRequestDTO payload) {
        String draftId = draftService.createDraft(payload.getAuthorId());
        return ResponseEntity
                .created(URI.create("/api/v1/drafts/" + draftId))
                .build();
    }

    @PostMapping("/{draftId}")
    public ResponseEntity<Map<String, String>> publishDraft(
            @Size(min = 24, max = 24, message = "Draft id must be 24 characters long") @PathVariable String draftId,
            @Valid @RequestBody PublishDraftRequestDTO payload) {

        String articleSlug = draftService.publishDraft(payload.getAuthorId(), draftId);
        return ResponseEntity.created(URI.create("/api/v1/articles/" + articleSlug))
                .body(Map.of("slug", articleSlug));
    }

    @GetMapping("/{draftId}")
    public DraftDTO getDraft(@Size(min = 24, max = 24, message = "Draft id must be 24 characters long") @PathVariable String draftId) {
        return draftService.getDraft(draftId);
    }

    @PutMapping("/{draftId}")
    public void editDraft(@NotBlank @PathVariable String draftId, @Valid @RequestBody DraftDTO payload, JwtAuthenticationToken authentication) {
        draftService.editDraft(draftId, authentication.getName(), payload);
    }
}
