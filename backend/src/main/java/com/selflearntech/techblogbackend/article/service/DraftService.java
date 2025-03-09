package com.selflearntech.techblogbackend.article.service;

import com.selflearntech.techblogbackend.article.dto.DraftDTO;
import com.selflearntech.techblogbackend.article.mapper.DraftMapper;
import com.selflearntech.techblogbackend.article.model.Article;
import com.selflearntech.techblogbackend.article.model.Draft;
import com.selflearntech.techblogbackend.article.repository.ArticleRepository;
import com.selflearntech.techblogbackend.article.repository.DraftRepository;
import com.selflearntech.techblogbackend.user.model.User;
import com.selflearntech.techblogbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class DraftService implements IDraftService{

    private final DraftRepository draftRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final Clock clock;
    private final DraftMapper draftMapper;


    @Override
    public String createDraft(String authorEmail) {
        User user = userRepository.findByEmail(authorEmail).orElseThrow(() -> new RuntimeException("Failed to assign draft to user"));
        Draft draft = Draft.builder()
                .createdAt(Instant.now(clock))
                .user(user)
                .build();
        draft = draftRepository.save(draft);
        return draft.getId();
    }

    @Override
    public String publishDraft(String authorEmail, String draftId) {
        Draft draft = draftRepository.findById(draftId).orElseThrow(() -> new RuntimeException("Could not find draft with id: " + draftId));

        if (!draft.getUser().getEmail().equals(authorEmail)) throw new RuntimeException("Draft author does not match");
        Article article = draft.getArticle();
        if (article == null) {
            article = new Article();
            article.setUser(draft.getUser());
            article.setSlug(generateArticleSlug(draft.getTitle()));
            article.setCreatedAt(Instant.now(clock));
        } else {
            article.setUpdatedAt(Instant.now(clock));
        }

        article.setTitle(draft.getTitle());
        article.setContent(draft.getContent());
        article.setDescription(draft.getDescription());
        article.setCoverImg(draft.getCoverImg());
        article.setCategory(draft.getCategory());

        article = articleRepository.save(article);
        if (draft.getArticle() == null) {
            draft.setArticle(article);
            draftRepository.save(draft);
        }

        return article.getSlug();
    }

    @Override
    public String generateArticleSlug(String draftTitle) {
        int iteration = 0;
        String slug = draftTitle.toLowerCase().replace(" ", "-");

        do {
            if (iteration > 0) {
                slug = slug.concat("-" + iteration);
            }
            iteration++;
        } while (articleRepository.existsBySlug(slug));

        return slug;
    }


    @Override
    public DraftDTO getDraft(String draftId) {
        Draft draft = draftRepository.findById(draftId).orElseThrow(() -> new RuntimeException(""));
        return draftMapper.toDraftDTO(draft);
    }

    @Override
    public void editDraft(String draftId, String authorEmail, DraftDTO payload) {
        Draft draft = draftRepository.findById(draftId).orElseThrow(() -> new RuntimeException(""));
        if (!draft.getUser().getEmail().equals(authorEmail)) throw new RuntimeException("Draft author does not match");

        draft.setTitle(payload.getTitle());
        draft.setCoverImg(payload.getCoverImg());
        draft.setContent(payload.getContent());
        draft.setDescription(payload.getDescription());
        draft.setCategory(payload.getCategory());
        draft.setUpdatedAt(Instant.now(clock));

        draftRepository.save(draft);
    }


}
