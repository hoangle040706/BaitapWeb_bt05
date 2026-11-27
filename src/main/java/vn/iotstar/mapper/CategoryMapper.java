package vn.iotstar.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import vn.iotstar.dto.CategoryDto;
import vn.iotstar.dto.CategoryRequest;
import vn.iotstar.entity.Category;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    CategoryRequest toRequest(Category category);

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(CategoryRequest request, @MappingTarget Category entity);
}
