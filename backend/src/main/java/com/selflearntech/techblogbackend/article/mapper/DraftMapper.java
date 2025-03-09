package com.selflearntech.techblogbackend.article.mapper;

import com.selflearntech.techblogbackend.article.dto.DraftDTO;
import com.selflearntech.techblogbackend.article.model.Draft;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DraftMapper {

    DraftDTO toDraftDTO(Draft draft);

}
