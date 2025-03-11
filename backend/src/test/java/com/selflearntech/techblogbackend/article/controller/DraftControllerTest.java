package com.selflearntech.techblogbackend.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selflearntech.techblogbackend.article.dto.CreateDraftRequestDTO;
import com.selflearntech.techblogbackend.article.dto.PublishDraftRequestDTO;
import com.selflearntech.techblogbackend.article.service.IDraftService;
import com.selflearntech.techblogbackend.config.SecurityConfig;
import com.selflearntech.techblogbackend.exception.ErrorMessages;
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

import static com.selflearntech.techblogbackend.utils.ResponseBodyMatchers.responseBody;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DraftController.class)
@Import({SecurityConfig.class, RSAKeyProperties.class})
class DraftControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserDetailsService userDetailsService;
    @MockitoBean
    private IDraftService draftService;

    private final static String DRAFT_ID = "67983098a363fb3d06132a51";


    @Nested
    public  class CreateDraft {

        @Test
        @WithMockUser(roles = "AUTHOR")
        void createDraft_WithAuthorIdIncluded_ShouldReturn2001StatusWithLocationHeader() throws Exception {
            // Given
            String authorId = "67983098a363fb3d06132a11";
            CreateDraftRequestDTO payload = new CreateDraftRequestDTO(authorId);

            given(draftService.createDraft(authorId)).willReturn(DRAFT_ID);


            // When
            mockMvc.perform(post("/api/v1/drafts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(header().string("Location", String.format("/api/v1/drafts/%s", DRAFT_ID)));

            // Then
            then(draftService).should().createDraft(authorId);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void createDraft_WithMissingRequestBody_ShouldReturn400StatusWithErrorMessage() throws Exception {
            // Given

            // When
            mockMvc.perform(post("/api/v1/drafts")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(responseBody().containsErrorMessage(ErrorMessages.MISSING_REQUEST_BODY));

            // Then
            then(draftService).should(never()).createDraft(any());
        }

        @Test
        @WithMockUser(roles = "AUTHOR")
        void createDraft_WithInvalidAuthorId_ShouldReturn400StatusWithValidationErrorMessage() throws Exception {
            // Given
            String authorId = "67983098a363fb3d06132a";
            CreateDraftRequestDTO payload = new CreateDraftRequestDTO(authorId);

            given(draftService.createDraft(authorId)).willReturn(DRAFT_ID);


            // When
            mockMvc.perform(post("/api/v1/drafts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isBadRequest())
                    .andExpect(responseBody().containsValidationError("authorId", "Author id must be 24 characters long"));


            // Then
            then(draftService).should(never()).createDraft(any());
        }
    }

    @Nested
    public class PublishDraft {

        @Test
        @WithMockUser(roles = "AUTHOR")
        void publishDraft_WithValidPayload_ShouldReturn201StatusWithLocationHeader() throws Exception {
            // Given
            String authorId = "67983098a363fb3d06132a11";
            PublishDraftRequestDTO payload = new PublishDraftRequestDTO(authorId);
            String articleSlug = "spring-boot-exception-handling";

            given(draftService.publishDraft(authorId, DRAFT_ID)).willReturn(articleSlug);

            // When
            mockMvc.perform(post("/api/v1/drafts/{draftId}", DRAFT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(header().string("Location", String.format("/api/v1/articles/%s", articleSlug)))
                    .andExpect(jsonPath("$.slug", is(articleSlug)));

            // Then
            then(draftService).should().publishDraft(authorId, DRAFT_ID);
        }

        @Test
        @WithMockUser(roles = "AUTHOR")
        void publishDraft_WithMissingRequestBody_ShouldReturn400StatusWithErrorMessage() throws Exception {
            // Given

            // When
            mockMvc.perform(post("/api/v1/drafts/{draftId}", DRAFT_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(responseBody().containsErrorMessage(ErrorMessages.MISSING_REQUEST_BODY));

            // Then
            then(draftService).should(never()).publishDraft(any(), any());
        }

        @Test
        @WithMockUser(roles = "AUTHOR")
        void publishDraft_WithInvalidAuthorId_ShouldReturn400StatusWithValidationErrorMessage() throws Exception {
            // Given
            String authorId = "67983098a363fb3d06132a112323";
            PublishDraftRequestDTO payload = new PublishDraftRequestDTO(authorId);

            // When
            mockMvc.perform(post("/api/v1/drafts/{draftId}", DRAFT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isBadRequest())
                    .andExpect(responseBody().containsValidationError("authorId", "Author id must be 24 characters long"));

            // Then
            then(draftService).should(never()).publishDraft(any(), any());
        }

        @Test
        void publishDraft_Unauthenticated_ShouldReturn401StatusWithErrorMessage() throws Exception {
            // Given

            // When
            mockMvc.perform(post("/api/v1/drafts/{draftId}", DRAFT_ID))
                    .andExpect(status().isUnauthorized());

            // Then
            then(draftService).should(never()).publishDraft(any(), any());
        }

        @Test
        @WithMockUser(roles = "USER")
        void publishDraft_Unauthorized_ShouldReturn403StatusWithErrorMessage() throws Exception {
            // Given

            // When
            mockMvc.perform(post("/api/v1/drafts/{draftId}", DRAFT_ID))
                    .andExpect(status().isForbidden());

            // Then
            then(draftService).should(never()).publishDraft(any(), any());
        }
    }



}
