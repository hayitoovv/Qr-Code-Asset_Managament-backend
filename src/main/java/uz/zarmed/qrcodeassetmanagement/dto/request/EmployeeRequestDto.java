package uz.zarmed.qrcodeassetmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String position;
    private Long departmentId;
}
