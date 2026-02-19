package uz.zarmed.qrcodeassetmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String qrCode;        // "AST-2026-00001"

    private String qrImageUrl;    // QR image path

    // ✅ Faqat QrCode dan Asset ga - bir tomonlama
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", unique = true)
    private Asset asset;

    private LocalDateTime generatedAt;

    @PrePersist
    public void prePersist() {
        this.generatedAt = LocalDateTime.now();
    }
}