package uz.zarmed.qrcodeassetmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.zarmed.qrcodeassetmanagement.entity.enums.Condition;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assasets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String assetName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(unique = true)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Enumerated(EnumType.STRING)
    private Condition condition;

    private LocalDate purchaseDate;
    private Double price;
    private LocalDate warrantyEndDate;
    private String description;

    // Location
    private String building;
    private Integer floor;
    private String room;

    private Integer scanCount = 0;
    private LocalDateTime lastScanned;

    // ✅ ManyToOne - bir employee ko'p assetga ega bo'lishi mumkin
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;  // NULL bo'lishi mumkin

    // ✅ QrCode ni olib tashladik - QrCode da Asset bor

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.scanCount == null) this.scanCount = 0;
    }
}