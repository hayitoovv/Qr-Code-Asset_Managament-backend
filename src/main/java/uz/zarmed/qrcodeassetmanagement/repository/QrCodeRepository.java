package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.zarmed.qrcodeassetmanagement.entity.QrCode;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
}