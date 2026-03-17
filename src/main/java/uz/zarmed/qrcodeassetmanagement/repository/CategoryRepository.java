package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.zarmed.qrcodeassetmanagement.dto.response.CategoryResponseDto;
import uz.zarmed.qrcodeassetmanagement.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsDeletedFalse();

    boolean existsByName(String categoryName);

    Optional<Category> findByIdAndIsDeletedFalse(Long id);
}