package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.zarmed.qrcodeassetmanagement.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentCode(String departmentCode);
}