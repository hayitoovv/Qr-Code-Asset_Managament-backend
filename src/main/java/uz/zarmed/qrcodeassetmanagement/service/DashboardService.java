package uz.zarmed.qrcodeassetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.zarmed.qrcodeassetmanagement.dto.response.DashboardStatsDto;
import uz.zarmed.qrcodeassetmanagement.repository.AssetRepository;
import uz.zarmed.qrcodeassetmanagement.repository.CategoryRepository;
import uz.zarmed.qrcodeassetmanagement.repository.DepartmentRepository;
import uz.zarmed.qrcodeassetmanagement.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;

    public DashboardStatsDto getDashboardStats() {
        //assets
        long totalAssets = assetRepository.countByIsDeletedFalse();
        long assignedAssets = assetRepository.countByStatusIdAndIsDeletedFalse(2L);
        Long availableAssets=assetRepository.countByStatusIdAndIsDeletedFalse(1L);
        Long inRepairAssets=assetRepository.countByStatusIdAndIsDeletedFalse(3L);

        //employee
        Long totalEmployees=employeeRepository.countByIsDeletedFalse();

        //department
        Long totalDepartments=departmentRepository.countByIsDeletedFalse();

        //catigories
        Long totalCategories=categoryRepository.countByIsDeletedFalse();

        DashboardStatsDto dto = DashboardStatsDto.builder()
                .totalAssets(totalAssets)
                .assignedAssets(assignedAssets)
                .availableAssets(availableAssets)
                .inRepairAssets(inRepairAssets)
                .totalDepartments(totalDepartments)
                .totalCategories(totalCategories)
                .totalEmployees(totalEmployees)
                .todayScans(5L)
                .build();

        return dto;
    }
}
