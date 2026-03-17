package uz.zarmed.qrcodeassetmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.zarmed.qrcodeassetmanagement.entity.enums.Condition;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeScanResponseDto {

    // QR Code Info
    private String qrCode;
    private String qrImageUrl;
    private LocalDateTime scannedAt;
    private Integer totalScans;

    // Asset Info
    private Long assetId;
    private String assetName;
    private String serialNumber;
    private Condition condition;
    private String description;

    // Category
    private String categoryName;
    private String categoryIcon;

    // Status
    private String statusName;
    private String statusColor;

    // Purchase Info
    private LocalDate purchaseDate;
    private Double price;
    private LocalDate warrantyEndDate;
    private Boolean isUnderWarranty;

    // Location
    private String building;
    private Integer floor;
    private String room;
    private String fullLocation;  // "HQ, Floor 3, Room 301"

    // Employee (if assigned)
    private EmployeeInfoDto assignedEmployee;

    // Department (if assigned)
    private DepartmentInfoDto department;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime lastScanned;
}

