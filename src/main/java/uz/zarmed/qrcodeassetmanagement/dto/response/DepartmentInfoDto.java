package uz.zarmed.qrcodeassetmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentInfoDto {
    private Long id;
    private String departmentName;
    private String departmentCode;
    private String departmentDescription;
}
