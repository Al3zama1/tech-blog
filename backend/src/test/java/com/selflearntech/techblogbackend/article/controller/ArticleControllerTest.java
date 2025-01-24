package com.selflearntech.techblogbackend.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selflearntech.techblogbackend.article.service.IArticleService;
import com.selflearntech.techblogbackend.config.SecurityConfig;
import com.selflearntech.techblogbackend.token.util.RSAKeyProperties;
import com.selflearntech.techblogbackend.user.service.UserDetailsService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@Import({SecurityConfig.class, RSAKeyProperties.class})
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserDetailsService userDetailsService;
    @MockitoBean
    private IArticleService articleService;

    @Nested
    class Draft {

        @Test
        @WithMockUser(roles = {"AUTHOR", "ADMIN"})
        void createDraft_ShouldReturn2001WithDraftId() throws Exception {
            // Given
            int draftId = 1;
            given(articleService.createDraft()).willReturn(draftId);

            // When
            mockMvc.perform(post("/api/v1/create-draft")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("location"))
                    .andExpect(header().string("location", "/api/v1/drafts/" + draftId));

            // Then
            then(articleService).should().createDraft();
        }
    }

}
