package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.CategoryModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ICategoryEntityMapper {

    @Mapping(target = "nombre", source = "name")
    @Mapping(target = "descripcion", source = "description")
    CategoryEntity toEntity(CategoryModel categoryModel);

    @Mapping(target = "name", source = "nombre")
    @Mapping(target = "description", source = "descripcion")
    CategoryModel toCategoryModel(CategoryEntity categoryEntity);

    List<CategoryModel> toCategoryModelList(List<CategoryEntity> categoryEntityList);
}
