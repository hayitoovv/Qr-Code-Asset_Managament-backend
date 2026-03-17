//package uz.zarmed.qrcodeassetmanagement.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//public class Category {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false)
//    private Long id;
//    private String name;
//    private LocalDateTime createdAt;
//
//}

package uz.zarmed.qrcodeassetmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity{

    @Column(unique = true, nullable = false)
    private String name;   // "Laptop", "Monitor", "Keyboard"

    private String icon;   // "Laptop", "Monitor" - frontend icon nomi
}