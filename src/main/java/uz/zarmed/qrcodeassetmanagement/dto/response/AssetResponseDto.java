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
public class AssetResponseDto {
    private Long id;
    private String assetName;
    private String serialNumber;

    // Category
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;

    // Status
    private Long statusId;
    private String statusName;

    // Details
    private Condition condition;
    private LocalDate purchaseDate;
    private Double price;
    private LocalDate warrantyEndDate;
    private String description;

    // Location
    private String building;
    private Integer floor;
    private String room;

    // Employee
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;

    // QR Code
    private String qrCode;
    private String qrImageUrl;

    // Tracking
    private Integer scanCount;
    private LocalDateTime lastScanned;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}