package com.selflearntech.techblogbackend.article.controller;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.exceptions.*;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class ImageKitController {

    private final Clock clock;
    private final ImageKit imageKit;

    @GetMapping("/auth-upload")
    public Map<String, String> authFileUpload() {
        String token = UUID.randomUUID().toString();
        long expire = Instant.now(clock).getEpochSecond() + 1800L;

        return imageKit.getAuthenticationParameters(token, expire);
    }

    @DeleteMapping("/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFile(@NotBlank @PathVariable String fileId) throws ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {
        imageKit.deleteFile(fileId);
    }

}
