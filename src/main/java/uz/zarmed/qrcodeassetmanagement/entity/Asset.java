package uz.zarmed.qrcodeassetmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import uz.zarmed.qrcodeassetmanagement.entity.enums.Condition;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset extends BaseEntity {

    @Column(nullable = false)
    private String assetName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(unique = true)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.EAGER)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne(mappedBy = "asset", cascade = CascadeType.ALL)
    private QrCode qrCode;

}