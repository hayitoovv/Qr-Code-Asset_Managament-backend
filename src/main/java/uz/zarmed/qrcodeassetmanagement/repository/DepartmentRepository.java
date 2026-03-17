package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.zarmed.qrcodeassetmanagement.entity.Department;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentCode(String departmentCode);

    List<Department> findByIsDeletedFalse();

    Optional<Department> findByIdAndIsDeletedFalse(Long id);


    Long countByIsDeletedFalse();
}