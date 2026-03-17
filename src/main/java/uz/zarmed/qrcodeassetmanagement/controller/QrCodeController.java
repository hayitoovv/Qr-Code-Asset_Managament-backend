package uz.zarmed.qrcodeassetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.zarmed.qrcodeassetmanagement.dto.response.QrCodeResponseDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.QrCodeScanResponseDto;
import uz.zarmed.qrcodeassetmanagement.service.QrCodeService;

@RestController
@RequestMapping("/api/qr-codes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;

    // ==========================================
    // GENERATE QR CODE
    // ==========================================

    @PostMapping("/generate/{assetId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<QrCodeResponseDto> generateQrCode(@PathVariable Long assetId) {
        return ResponseEntity.ok(qrCodeService.generateQrCode(assetId));
    }

    // ==========================================
    // SCAN QR CODE - To'liq ma'lumot
    // ==========================================

    @PostMapping("/scan/{qrCode}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<QrCodeScanResponseDto> scanQrCode(@PathVariable String qrCode) {
        return ResponseEntity.ok(qrCodeService.scanQrCode(qrCode));
    }

    @GetMapping("/scan/{qrCode}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<QrCodeScanResponseDto> getQrCodeDetails(@PathVariable String qrCode) {
        return ResponseEntity.ok(qrCodeService.getQrCodeDetails(qrCode));
    }

    // ==========================================
    // GET QR CODE BY ASSET
    // ==========================================

    @GetMapping("/asset/{assetId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<QrCodeResponseDto> getQrCodeByAssetId(@PathVariable Long assetId) {
        return ResponseEntity.ok(qrCodeService.getQrCodeByAssetId(assetId));
    }

    // ==========================================
    // REGENERATE QR CODE
    // ==========================================

    @PutMapping("/regenerate/{assetId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<QrCodeResponseDto> regenerateQrCode(@PathVariable Long assetId) {
        return ResponseEntity.ok(qrCodeService.regenerateQrCode(assetId));
    }

    // ==========================================
    // DELETE QR CODE
    // ==========================================

    @DeleteMapping("/asset/{assetId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteQrCode(@PathVariable Long assetId) {
        qrCodeService.deleteQrCode(assetId);
        return ResponseEntity.noContent().build();
    }
}