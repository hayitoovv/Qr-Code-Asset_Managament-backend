package uz.zarmed.qrcodeassetmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.zarmed.qrcodeassetmanagement.dto.request.AssetRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.AssetResponseDto;
import uz.zarmed.qrcodeassetmanagement.entity.*;
import uz.zarmed.qrcodeassetmanagement.exception.BusinessException;
import uz.zarmed.qrcodeassetmanagement.exception.ErrorCode;
import uz.zarmed.qrcodeassetmanagement.exception.ResourceNotFoundException;
import uz.zarmed.qrcodeassetmanagement.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {

    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditService auditService;

    // ==========================================
    // GET OPERATIONS
    // ==========================================

    public List<AssetResponseDto> getAllAssets() {
        return assetRepository.findByIsDeletedFalse()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AssetResponseDto getAssetById(Long id) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + id));
        return toDto(asset);
    }

    public List<AssetResponseDto> getFilteredAssets(Long statusId, Long categoryId, Long employeeId) {
        return assetRepository.findByFilters(statusId, categoryId, employeeId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<AssetResponseDto> searchAssets(String searchTerm) {
        return assetRepository.searchAssets(searchTerm)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ==========================================
    // CREATE
    // ==========================================

    @Transactional
    public AssetResponseDto createAsset(AssetRequestDto dto, String createdBy) {
        // Validation: Serial number exists?
        if (dto.getSerialNumber() != null &&
                assetRepository.existsBySerialNumber(dto.getSerialNumber())) {
            throw new BusinessException(
                    ErrorCode.SERIAL_EXISTS,
                    String.format("Asset with serial number '%s' already exists", dto.getSerialNumber())
            );
        }

        // Fetch related entities
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + dto.getCategoryId()));



        // Default status: Available (id=1)
        Status defaultStatus = statusRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Default status 'Available' not found"));

        Employee employee = null;
        if (dto.getEmployeeId() != null) {
            employee = employeeRepository.findByIdAndIsDeletedFalse(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + dto.getEmployeeId()));
        }

        // Build asset
        Asset asset = Asset.builder()
                .assetName(dto.getAssetName())
                .category(category)
                .serialNumber(dto.getSerialNumber())
                .status(defaultStatus)
                .condition(dto.getCondition())
                .purchaseDate(dto.getPurchaseDate())
                .price(dto.getPrice())
                .warrantyEndDate(dto.getWarrantyEndDate())
                .description(dto.getDescription())
                .building(dto.getBuilding())
                .floor(dto.getFloor())
                .room(dto.getRoom())
                .employee(employee)
                .build();

        Asset saved = assetRepository.save(asset);

        // Audit log
        auditService.logCreate("Asset", saved.getId(), saved, createdBy);

        log.info("Asset created: {} by {}", saved.getId(), createdBy);
        return toDto(saved);
    }

    // ==========================================
    // UPDATE
    // ==========================================

    @Transactional
    public AssetResponseDto updateAsset(Long id, AssetRequestDto dto, String updatedBy) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + id));

        // Validation: Serial number exists in another asset?
        if (dto.getSerialNumber() != null &&
                assetRepository.existsBySerialNumberAndIdNot(dto.getSerialNumber(), id)) {
            throw new BusinessException(
                    ErrorCode.SERIAL_EXISTS,
                    String.format("Asset with serial number '%s' already exists", dto.getSerialNumber())
            );
        }

        // Old value for audit
        Asset oldAsset = Asset.builder()
                .assetName(asset.getAssetName())
                .serialNumber(asset.getSerialNumber())
                .build();

        // Fetch related entities
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + dto.getCategoryId()));

        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found: " + dto.getStatusId()));

        Employee employee = null;
        if (dto.getEmployeeId() != null) {
            employee = employeeRepository.findByIdAndIsDeletedFalse(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + dto.getEmployeeId()));
        }

        // Update
        asset.setAssetName(dto.getAssetName());
        asset.setCategory(category);
        asset.setSerialNumber(dto.getSerialNumber());
        asset.setStatus(status);
        asset.setCondition(dto.getCondition());
        asset.setPurchaseDate(dto.getPurchaseDate());
        asset.setPrice(dto.getPrice());
        asset.setWarrantyEndDate(dto.getWarrantyEndDate());
        asset.setDescription(dto.getDescription());
        asset.setBuilding(dto.getBuilding());
        asset.setFloor(dto.getFloor());
        asset.setRoom(dto.getRoom());
        asset.setEmployee(employee);

        Asset updated = assetRepository.save(asset);

        // Audit log
        auditService.logUpdate("Asset", id, oldAsset, updated, updatedBy);

        log.info("Asset updated: {} by {}", id, updatedBy);
        return toDto(updated);
    }

    // ==========================================
    // DELETE
    // ==========================================

    @Transactional
    public void deleteAsset(Long id, String deletedBy) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + id));

        // Validation: Assigned to employee?
        if (asset.getEmployee() != null) {
            throw new BusinessException(
                    ErrorCode.ASSET_ASSIGNED,
                    String.format("Cannot delete asset '%s': currently assigned to employee '%s'. " +
                                    "Please unassign first.",
                            asset.getAssetName(),
                            asset.getEmployee().getFullName())
            );
        }

        // Audit log
        auditService.logDelete("Asset", id, asset, deletedBy);

        // Soft delete
        asset.setIsDeleted(true);
        asset.setDeletedAt(LocalDateTime.now());
        asset.setDeletedBy(deletedBy);
        assetRepository.save(asset);

        log.info("Asset soft deleted: {} by {}", id, deletedBy);
    }

    // ==========================================
    // ASSIGN/UNASSIGN
    // ==========================================

    @Transactional
    public AssetResponseDto assignAsset(Long assetId, Long employeeId, String performedBy) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));

        // Validation: Already assigned?
        if (asset.getEmployee() != null) {
            throw new BusinessException(
                    ErrorCode.ASSET_ALREADY_ASSIGNED,
                    String.format("Asset '%s' is already assigned to '%s'",
                            asset.getAssetName(),
                            asset.getEmployee().getFullName())
            );
        }

        // Get "Assigned" status
        Status assignedStatus = statusRepository.findByStatus("Assigned")
                .orElseThrow(() -> new ResourceNotFoundException("Status 'Assigned' not found"));

        // Assign
        asset.setEmployee(employee);
        asset.setStatus(assignedStatus);
        Asset updated = assetRepository.save(asset);

        log.info("Asset {} assigned to employee {} by {}", assetId, employeeId, performedBy);
        return toDto(updated);
    }

    @Transactional
    public AssetResponseDto unassignAsset(Long assetId, String performedBy) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        // Get "Available" status
        Status availableStatus = statusRepository.findByStatus("Available")
                .orElseThrow(() -> new ResourceNotFoundException("Status 'Available' not found"));

        // Unassign
        asset.setEmployee(null);
        asset.setStatus(availableStatus);
        Asset updated = assetRepository.save(asset);

        log.info("Asset {} unassigned by {}", assetId, performedBy);
        return toDto(updated);
    }

    // ==========================================
    // STATUS CHANGE
    // ==========================================

    @Transactional
    public AssetResponseDto changeStatus(Long assetId, Long statusId, String performedBy) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        Status newStatus = statusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found: " + statusId));

        String statusName = newStatus.getStatus();

        // Business rules
        if ("Assigned".equals(statusName) && asset.getEmployee() == null) {
            throw new BusinessException(
                    ErrorCode.ASSET_NOT_ASSIGNED,
                    "Cannot set status to 'Assigned' without an employee"
            );
        }

        // Auto-unassign for certain statuses
        if (("In Repair".equals(statusName) ||
                "Lost".equals(statusName) ||
                "Retired".equals(statusName)) &&
                asset.getEmployee() != null) {
            asset.setEmployee(null);
            log.info("Asset {} auto-unassigned due to status change to {}", assetId, statusName);
        }

        asset.setStatus(newStatus);
        Asset updated = assetRepository.save(asset);

        log.info("Asset {} status changed to {} by {}", assetId, statusName, performedBy);
        return toDto(updated);
    }

    // ==========================================
    // SCAN
    // ==========================================

    @Transactional
    public AssetResponseDto scanAsset(Long assetId) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        // Increment scan count
        asset.setScanCount(asset.getScanCount() + 1);
        asset.setLastScanned(LocalDateTime.now());

        Asset updated = assetRepository.save(asset);

        log.info("Asset {} scanned. Total scans: {}", assetId, updated.getScanCount());
        return toDto(updated);
    }

    // ==========================================
    // RESTORE
    // ==========================================

    @Transactional
    public AssetResponseDto restoreAsset(Long assetId, String restoredBy) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        if (!asset.getIsDeleted()) {
            throw new BusinessException("ASSET_NOT_DELETED", "Asset is not deleted");
        }

        // Restore
        asset.setIsDeleted(false);
        asset.setDeletedAt(null);
        asset.setDeletedBy(null);

        Asset restored = assetRepository.save(asset);

        // Audit log
        auditService.logRestore("Asset", assetId, restored, restoredBy);

        log.info("Asset restored: {} by {}", assetId, restoredBy);
        return toDto(restored);
    }

    // ==========================================
    // DTO CONVERTER
    // ==========================================

    private AssetResponseDto toDto(Asset asset) {
        return AssetResponseDto.builder()
                .id(asset.getId())
                .assetName(asset.getAssetName())
                .serialNumber(asset.getSerialNumber())
                .categoryId(asset.getCategory().getId())
                .categoryName(asset.getCategory().getName())
                .categoryIcon(asset.getCategory().getIcon())
                .statusId(asset.getStatus().getId())
                .statusName(asset.getStatus().getStatus())
                .condition(asset.getCondition())
                .purchaseDate(asset.getPurchaseDate())
                .price(asset.getPrice())
                .warrantyEndDate(asset.getWarrantyEndDate())
                .description(asset.getDescription())
                .building(asset.getBuilding())
                .floor(asset.getFloor())
                .room(asset.getRoom())
                .employeeId(asset.getEmployee() != null ? asset.getEmployee().getId() : null)
                .employeeName(asset.getEmployee() != null ? asset.getEmployee().getFullName() : null)
                .employeeEmail(asset.getEmployee() != null ? asset.getEmployee().getEmail() : null)
                .qrCode(asset.getQrCode() != null ? asset.getQrCode().getQrCode() : null)
                .qrImageUrl(asset.getQrCode() != null ? asset.getQrCode().getQrImageUrl() : null)
                .scanCount(asset.getScanCount())
                .lastScanned(asset.getLastScanned())
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdateAt())
                .build();
    }
}