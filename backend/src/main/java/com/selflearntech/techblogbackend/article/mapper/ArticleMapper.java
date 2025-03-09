package com.selflearntech.techblogbackend.article.mapper;

import com.selflearntech.techblogbackend.article.dto.ArticleDTO;
import com.selflearntech.techblogbackend.article.dto.ArticlePageEntryDTO;
import com.selflearntech.techblogbackend.article.dto.AuthorDTO;
import com.selflearntech.techblogbackend.article.model.Article;
import com.selflearntech.techblogbackend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "createdAt", expression = "java(article.getCreatedAt())")
    @Mapping(target = "coverImg", expression = "java(article.getCoverImg().getFilePath())")
    ArticleDTO toArticleDTO(Article article, AuthorDTO author);


    @Mapping(target = "createdAt", expression = "java(article.getCreatedAt())")
    @Mapping(target = "img", expression = "java(article.getCoverImg().getFilePath())")
    ArticlePageEntryDTO toArticlePreviewDTO(Article article, AuthorDTO author);

    default String getAuthor(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}

