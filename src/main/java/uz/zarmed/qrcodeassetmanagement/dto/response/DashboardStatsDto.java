package uz.zarmed.qrcodeassetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDto {
    private Long totalAssets;
    private Long assignedAssets;
    private Long availableAssets;
    private Long inRepairAssets;
    private Long totalEmployees;
    private Long totalDepartments;
    private Long totalCategories;
    private Long todayScans;
}