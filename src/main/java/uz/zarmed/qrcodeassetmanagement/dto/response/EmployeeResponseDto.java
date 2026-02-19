package uz.zarmed.qrcodeassetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String position;
    private Long departmentId;
    private LocalDateTime createdAt;
}
