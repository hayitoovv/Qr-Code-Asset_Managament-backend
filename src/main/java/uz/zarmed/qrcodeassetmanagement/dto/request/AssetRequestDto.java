package uz.zarmed.qrcodeassetmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.zarmed.qrcodeassetmanagement.entity.enums.Condition;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetRequestDto {
    private String assetName;
    private Long categoryId;
    private String serialNumber;
    private Long statusId;
    private Condition condition;
    private LocalDate purchaseDate;
    private Double price;
    private LocalDate warrantyEndDate;
    private String description;
    private String building;
    private Integer floor;
    private String room;
    private Long employeeId;
}