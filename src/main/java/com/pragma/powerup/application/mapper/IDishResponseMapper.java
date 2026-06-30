package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.DishListResponseDto;
import com.pragma.powerup.domain.model.DishModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishResponseMapper {

    @Mapping(target = "categoria", source = "categoria.name")
    DishListResponseDto toListResponse(DishModel dishModel);

    List<DishListResponseDto> toListResponseList(List<DishModel> dishModelList);
}
