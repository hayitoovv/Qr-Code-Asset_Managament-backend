package uz.zarmed.qrcodeassetmanagement.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.zarmed.qrcodeassetmanagement.dto.response.*;
import uz.zarmed.qrcodeassetmanagement.entity.*;
import uz.zarmed.qrcodeassetmanagement.exception.BusinessException;
import uz.zarmed.qrcodeassetmanagement.exception.ResourceNotFoundException;
import uz.zarmed.qrcodeassetmanagement.repository.AssetRepository;
import uz.zarmed.qrcodeassetmanagement.repository.QrCodeRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final AssetRepository assetRepository;

    private static final String QR_CODE_PREFIX = "AST";
    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;

    // ==========================================
    // GENERATE QR CODE
    // ==========================================

    @Transactional
    public QrCodeResponseDto generateQrCode(Long assetId) {
        // Check if asset exists
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        // Check if QR code already exists
        if (qrCodeRepository.existsByAssetId(assetId)) {
            throw new BusinessException(
                    "QR_CODE_EXISTS",
                    String.format("QR Code already exists for asset '%s'", asset.getAssetName())
            );
        }

        // Generate QR code value
        String qrCodeValue = generateQrCodeValue(assetId);

        // Generate QR code image
        String qrImageBase64 = generateQrCodeImage(qrCodeValue);

        // Save to database
        QrCode qrCode = QrCode.builder()
                .qrCode(qrCodeValue)
                .qrImageUrl(qrImageBase64)  // Base64 string
                .asset(asset)
                .build();

        QrCode saved = qrCodeRepository.save(qrCode);

        log.info("QR Code generated for asset: {}", assetId);
        return toDto(saved);
    }

    // ==========================================
    // SCAN QR CODE
    // ==========================================

    @Transactional
    public QrCodeScanResponseDto scanQrCode(String qrCode) {
        // Find QR code with full details
        QrCode qrCodeEntity = qrCodeRepository.findByQrCodeWithFullDetails(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found: " + qrCode));

        Asset asset = qrCodeEntity.getAsset();

        // Update scan count
        asset.setScanCount(asset.getScanCount() + 1);
        asset.setLastScanned(LocalDateTime.now());
        assetRepository.save(asset);

        log.info("QR Code scanned: {}. Total scans: {}", qrCode, asset.getScanCount());

        // Return detailed response
        return toScanDto(qrCodeEntity, asset);
    }

    // ==========================================
    // GET QR CODE
    // ==========================================

    public QrCodeResponseDto getQrCodeByAssetId(Long assetId) {
        QrCode qrCode = qrCodeRepository.findByAssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "QR Code not found for asset: " + assetId
                ));
        return toDto(qrCode);
    }

    public QrCodeScanResponseDto getQrCodeDetails(String qrCode) {
        QrCode qrCodeEntity = qrCodeRepository.findByQrCodeWithFullDetails(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found: " + qrCode));

        return toScanDto(qrCodeEntity, qrCodeEntity.getAsset());
    }

    // ==========================================
    // REGENERATE QR CODE
    // ==========================================

    @Transactional
    public QrCodeResponseDto regenerateQrCode(Long assetId) {
        // Find existing QR code
        QrCode existingQrCode = qrCodeRepository.findByAssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "QR Code not found for asset: " + assetId
                ));

        // Generate new QR code value
        String newQrCodeValue = generateQrCodeValue(assetId);

        // Generate new QR code image
        String qrImageBase64 = generateQrCodeImage(newQrCodeValue);

        // Update
        existingQrCode.setQrCode(newQrCodeValue);
        existingQrCode.setQrImageUrl(qrImageBase64);

        QrCode updated = qrCodeRepository.save(existingQrCode);

        log.info("QR Code regenerated for asset: {}", assetId);
        return toDto(updated);
    }

    // ==========================================
    // DELETE QR CODE
    // ==========================================

    @Transactional
    public void deleteQrCode(Long assetId) {
        QrCode qrCode = qrCodeRepository.findByAssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "QR Code not found for asset: " + assetId
                ));

        qrCodeRepository.delete(qrCode);
        log.info("QR Code deleted for asset: {}", assetId);
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    private String generateQrCodeValue(Long assetId) {
        // Format: AST-2026-00001
        int year = LocalDate.now().getYear();
        String formattedId = String.format("%05d", assetId);
        return String.format("%s-%d-%s", QR_CODE_PREFIX, year, formattedId);
    }

    private String generateQrCodeImage(String qrCodeValue) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrCodeValue,
                    BarcodeFormat.QR_CODE,
                    QR_CODE_WIDTH,
                    QR_CODE_HEIGHT,
                    hints
            );

            // Convert to Base64
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);

        } catch (WriterException | IOException e) {
            log.error("Error generating QR code image", e);
            throw new BusinessException("QR_GENERATION_ERROR", "Failed to generate QR code image");
        }
    }

    // ==========================================
    // DTO CONVERTERS
    // ==========================================

    private QrCodeResponseDto toDto(QrCode qrCode) {
        return QrCodeResponseDto.builder()
                .id(qrCode.getId())
                .qrCode(qrCode.getQrCode())
                .qrImageUrl(qrCode.getQrImageUrl())
                .generatedAt(qrCode.getCreatedAt())
                .assetId(qrCode.getAsset().getId())
                .build();
    }

    private QrCodeScanResponseDto toScanDto(QrCode qrCode, Asset asset) {
        // Check warranty status
        boolean isUnderWarranty = false;
        if (asset.getWarrantyEndDate() != null) {
            isUnderWarranty = asset.getWarrantyEndDate().isAfter(LocalDate.now());
        }

        // Build full location
        String fullLocation = String.format("%s, Floor %d, Room %s",
                asset.getBuilding() != null ? asset.getBuilding() : "Unknown",
                asset.getFloor() != null ? asset.getFloor() : 0,
                asset.getRoom() != null ? asset.getRoom() : "Unknown"
        );

        // Employee info
        EmployeeInfoDto employeeInfo = null;
        if (asset.getEmployee() != null) {
            Employee emp = asset.getEmployee();
            employeeInfo = EmployeeInfoDto.builder()
                    .id(emp.getId())
                    .fullName(emp.getFullName())
                    .email(emp.getEmail())
                    .phoneNumber(emp.getPhoneNumber())
                    .position(emp.getPosition())
                    .build();
        }

        // Department info
        DepartmentInfoDto departmentInfo = null;
        if (asset.getEmployee() != null && asset.getEmployee().getDepartment() != null) {
            Department dept = asset.getEmployee().getDepartment();
            departmentInfo = DepartmentInfoDto.builder()
                    .id(dept.getId())
                    .departmentName(dept.getDepartmentName())
                    .departmentCode(dept.getDepartmentCode())
                    .departmentDescription(dept.getDepartmentDescription())
                    .build();
        }

        return QrCodeScanResponseDto.builder()
                // QR Code Info
                .qrCode(qrCode.getQrCode())
                .qrImageUrl(qrCode.getQrImageUrl())
                .scannedAt(LocalDateTime.now())
                .totalScans(asset.getScanCount())
                // Asset Info
                .assetId(asset.getId())
                .assetName(asset.getAssetName())
                .serialNumber(asset.getSerialNumber())
                .condition(asset.getCondition())
                .description(asset.getDescription())
                // Category
                .categoryName(asset.getCategory().getName())
                .categoryIcon(asset.getCategory().getIcon())
                // Status
                .statusName(asset.getStatus().getStatus())
                // Purchase Info
                .purchaseDate(asset.getPurchaseDate())
                .price(asset.getPrice())
                .warrantyEndDate(asset.getWarrantyEndDate())
                .isUnderWarranty(isUnderWarranty)
                // Location
                .building(asset.getBuilding())
                .floor(asset.getFloor())
                .room(asset.getRoom())
                .fullLocation(fullLocation)
                // Assignment
                .assignedEmployee(employeeInfo)
                .department(departmentInfo)
                // Timestamps
                .createdAt(asset.getCreatedAt())
                .lastScanned(asset.getLastScanned())
                .build();
    }
}