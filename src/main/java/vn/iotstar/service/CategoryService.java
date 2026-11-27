package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.dto.CategoryDto;
import vn.iotstar.dto.CategoryRequest;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryDto> findAllActive();

    Page<CategoryDto> findActivePaginated(String keyword, Pageable page);

    Page<CategoryDto> findAll(Pageable page);

    Page<CategoryDto> findByNameContaining(String keyword, Pageable page);

    Optional<CategoryRequest> findRequestById(Long id);

    CategoryDto save(CategoryRequest request);

    void deleteById(Long id);

    long count();
}
