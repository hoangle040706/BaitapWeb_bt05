package vn.iotstar.service;

import vn.iotstar.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> findAllActive();

    long count();
}
