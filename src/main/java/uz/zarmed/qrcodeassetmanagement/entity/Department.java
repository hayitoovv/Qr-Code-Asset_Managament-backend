package uz.zarmed.qrcodeassetmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department extends BaseEntity {

    @Column(nullable = false)
    private String departmentName;

    @Column(unique = true, nullable = false)
    private String departmentCode;

    private String departmentDescription;
}
