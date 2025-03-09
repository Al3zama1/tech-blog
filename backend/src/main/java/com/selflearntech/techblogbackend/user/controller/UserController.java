package com.selflearntech.techblogbackend.user.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @PostMapping("/{userId}/bookmarks/{slug}")
    public void bookMarkArticle(@NotBlank @PathVariable String userId, @NotBlank String slug) {

    }

    @DeleteMapping("/{userId}/bookmarks/{slug}")
    public void deleteBookmarkArticle(@NotBlank @PathVariable String userId, @NotBlank String slug) {

    }
}
