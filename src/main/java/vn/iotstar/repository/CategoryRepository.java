package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByNameContaining(String keyword, Pageable page);

    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable page);

    Page<Category> findByStatus(Integer status, Pageable page);

    Page<Category> findByStatusAndNameContainingIgnoreCase(Integer status, String keyword, Pageable page);

    List<Category> findByStatusOrderByNameAsc(Integer status);
}
