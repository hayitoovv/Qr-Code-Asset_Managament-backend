package uz.zarmed.qrcodeassetmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "qr_codes")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCode extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String qrCode;  // AST-2026-00001

    @Column(length = 1000)
    private String qrImageUrl;  // /qr-images/AST-2026-00001.png

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;
}