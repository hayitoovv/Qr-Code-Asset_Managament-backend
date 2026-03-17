package uz.zarmed.qrcodeassetmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseEntity{

    @NotBlank
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String phoneNumber;

    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
   private Department department;
}
