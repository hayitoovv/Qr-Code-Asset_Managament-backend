package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.zarmed.qrcodeassetmanagement.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);

    Long countByDepartmentIdAndIsDeletedFalse(Long departmentId);

    List<Employee> findByDepartmentIdAndIsDeletedFalse(Long departmentId);

    List<Employee> findByIsDeletedFalse();

    Optional<Employee> findByIdAndIsDeletedFalse(Long id);

    Long countByIsDeletedFalse();
}