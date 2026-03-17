package uz.zarmed.qrcodeassetmanagement.dto.request;

import lombok.Data;
import uz.zarmed.qrcodeassetmanagement.entity.enums.UserRole;

@Data
public class RegisterRequestDto {
    private String fullName;
    private String email;
    private String password;
    private UserRole role;
    private String phoneNumber;
}