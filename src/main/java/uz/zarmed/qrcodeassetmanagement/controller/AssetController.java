package uz.zarmed.qrcodeassetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.zarmed.qrcodeassetmanagement.dto.request.AssetAssignDto;
import uz.zarmed.qrcodeassetmanagement.dto.request.AssetRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.request.AssetStatusChangeDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.AssetResponseDto;
import uz.zarmed.qrcodeassetmanagement.service.AssetService;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    // ==========================================
    // CRUD OPERATIONS
    // ==========================================

    // GET ALL
    @PreAuthorize("permitAll()") // ALL - hamma ko'ra oladi
    @GetMapping
    public ResponseEntity<List<AssetResponseDto>> getAllAssets(
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String search) {

        // Search
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(assetService.searchAssets(search));
        }

        // Filter
        if (statusId != null || categoryId != null || employeeId != null) {
            return ResponseEntity.ok(assetService.getFilteredAssets(statusId, categoryId, employeeId));
        }

        // All
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    // GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()") // ALL
    public ResponseEntity<AssetResponseDto> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AssetResponseDto> createAsset(@RequestBody AssetRequestDto dto) {
        String currentUser = "admin@company.com";
        return ResponseEntity.ok(assetService.createAsset(dto, currentUser));
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AssetResponseDto> updateAsset(
            @PathVariable Long id,
            @RequestBody AssetRequestDto dto) {
        String currentUser = "admin@company.com";
        return ResponseEntity.ok(assetService.updateAsset(id, dto, currentUser));
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        String currentUser = "admin@company.com";
        assetService.deleteAsset(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // ASSIGN/UNASSIGN
    // ==========================================

    // ASSIGN
    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AssetResponseDto> assignAsset(
            @PathVariable Long id,
            @RequestBody AssetAssignDto dto) {
        String currentUser = "admin@company.com";
        return ResponseEntity.ok(assetService.assignAsset(id, dto.getEmployeeId(), currentUser));
    }

    // UNASSIGN
    @PostMapping("/{id}/unassign")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AssetResponseDto> unassignAsset(@PathVariable Long id) {
        String currentUser = "admin@company.com";
        return ResponseEntity.ok(assetService.unassignAsset(id, currentUser));
    }

    // ==========================================
    // STATUS
    // ==========================================

    // CHANGE STATUS
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AssetResponseDto> changeStatus(
            @PathVariable Long id,
            @RequestBody AssetStatusChangeDto dto) {
        String currentUser = "admin@company.com";
        return ResponseEntity.ok(assetService.changeStatus(id, dto.getStatusId(), currentUser));
    }

    // ==========================================
    // SCAN
    // ==========================================

    // SCAN QR CODE
    @PostMapping("/{id}/scan")
    public ResponseEntity<AssetResponseDto> scanAsset(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.scanAsset(id));
    }

    // ==========================================
    // RESTORE
    // ==========================================

    // RESTORE
    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AssetResponseDto> restoreAsset(@PathVariable Long id) {
        String currentUser = "admin@company.com";
        return ResponseEntity.ok(assetService.restoreAsset(id, currentUser));
    }
}