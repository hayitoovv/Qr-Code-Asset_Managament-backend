package uz.zarmed.qrcodeassetmanagement.dto.request;

import lombok.Builder;
import lombok.Data;


@Data
public class DepartmentRequestDto {
    private String departmentName;
    private String departmentCode;
    private String departmentDescription;
}
