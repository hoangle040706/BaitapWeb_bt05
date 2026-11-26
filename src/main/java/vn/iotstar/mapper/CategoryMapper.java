package vn.iotstar.mapper;

import org.springframework.stereotype.Component;
import vn.iotstar.dto.CategoryDto;
import vn.iotstar.entity.Category;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .images(category.getImages())
                .status(category.getStatus())
                .build();
    }
}
