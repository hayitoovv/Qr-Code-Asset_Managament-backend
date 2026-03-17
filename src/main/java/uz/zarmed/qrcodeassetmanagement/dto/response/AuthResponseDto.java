package uz.zarmed.qrcodeassetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.zarmed.qrcodeassetmanagement.entity.enums.UserRole;

@Data
@Builder
public class AuthResponseDto {
    private String token;
    private String email;
    private String fullName;
    private UserRole role;
    private String message;
}