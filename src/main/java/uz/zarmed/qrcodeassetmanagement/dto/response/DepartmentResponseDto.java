package uz.zarmed.qrcodeassetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class DepartmentResponseDto {
    private Long id;
    private String departmentName;
    private String departmentCode;
    private String departmentDescription;
    private LocalDateTime createdAt;
}
