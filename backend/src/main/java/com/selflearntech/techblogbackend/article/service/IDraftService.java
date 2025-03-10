package com.selflearntech.techblogbackend.article.service;

import com.selflearntech.techblogbackend.article.dto.DraftDTO;

public interface IDraftService {

    String createDraft(String authorId);
    String publishDraft(String authorEmail, String draftId);
    String generateArticleSlug(String draftTitle);
    DraftDTO getDraft(String draftId);
    void editDraft(String draftId, String authorEmail, DraftDTO payload);

}
