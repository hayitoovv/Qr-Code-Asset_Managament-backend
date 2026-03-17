package uz.zarmed.qrcodeassetmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Nested DTOs
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfoDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String position;
    private String avatar;
}
