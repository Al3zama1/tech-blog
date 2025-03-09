package com.selflearntech.techblogbackend.article.mapper;

import com.selflearntech.techblogbackend.article.dto.AuthorDTO;
import com.selflearntech.techblogbackend.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface AuthorMapper {

    AuthorDTO toAuthorDTO(User user);
}
