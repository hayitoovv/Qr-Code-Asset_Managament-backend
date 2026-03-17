package uz.zarmed.qrcodeassetmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetStatusChangeDto {
    private Long statusId;
    private String reason;
}