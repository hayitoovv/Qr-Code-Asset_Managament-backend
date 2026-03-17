package uz.zarmed.qrcodeassetmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.zarmed.qrcodeassetmanagement.entity.enums.UserRole;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private String phoneNumber;

    @Column(nullable = false)
    private Boolean isActive = true;
}