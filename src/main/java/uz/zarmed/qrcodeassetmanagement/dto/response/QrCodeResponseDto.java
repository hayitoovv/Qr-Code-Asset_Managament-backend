package uz.zarmed.qrcodeassetmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeResponseDto {
    private Long id;
    private String qrCode;
    private String qrImageUrl;
    private LocalDateTime generatedAt;
    private Long assetId;
}