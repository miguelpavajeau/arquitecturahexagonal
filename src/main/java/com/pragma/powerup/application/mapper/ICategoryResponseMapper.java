
package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.CategoryResponseDto;
import com.pragma.powerup.domain.model.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface ICategoryResponseMapper {
    ICategoryResponseMapper INSTANCE = Mappers.getMapper(ICategoryResponseMapper.class);

    List<CategoryResponseDto> toCategoryResponseDtoList(List<CategoryModel> categoryModels);
}