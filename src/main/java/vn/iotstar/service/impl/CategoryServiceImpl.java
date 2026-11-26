package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.iotstar.dto.CategoryDto;
import vn.iotstar.entity.Category;
import vn.iotstar.exception.BusinessException;
import vn.iotstar.mapper.CategoryMapper;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAllActive() {
        return categoryRepository.findByStatusOrderByNameAsc(1)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> findActivePaginated(String keyword, Pageable page) {
        if (StringUtils.hasText(keyword)) {
            return categoryRepository.findByStatusAndNameContainingIgnoreCase(1, keyword.trim(), page)
                    .map(categoryMapper::toDto);
        }
        return categoryRepository.findByStatus(1, page).map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> findAll(Pageable page) {
        return categoryRepository.findAll(page).map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> findByNameContaining(String keyword, Pageable page) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword.trim(), page)
                .map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryDto> findById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto);
    }

    @Override
    @Transactional
    public CategoryDto save(CategoryDto dto) {
        Category entity;
        if (dto.getId() != null) {
            entity = categoryRepository.findById(dto.getId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy danh mục cần cập nhật"));
        } else {
            entity = new Category();
        }
        entity.setName(dto.getName().trim());
        entity.setImages(StringUtils.hasText(dto.getImages()) ? dto.getImages().trim() : null);
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BusinessException("Danh mục không tồn tại");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return categoryRepository.count();
    }
}
