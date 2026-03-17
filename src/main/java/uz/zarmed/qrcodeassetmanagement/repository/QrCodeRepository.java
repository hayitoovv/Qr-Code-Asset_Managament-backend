package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.zarmed.qrcodeassetmanagement.entity.QrCode;

import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Long> {

    // Find by QR code value
    Optional<QrCode> findByQrCode(String qrCode);

    // Find by asset
    Optional<QrCode> findByAssetId(Long assetId);

    // Check if exists
    boolean existsByQrCode(String qrCode);
    boolean existsByAssetId(Long assetId);

    // Find with full asset info (for scanning)
    @Query("SELECT q FROM QrCode q " +
            "LEFT JOIN FETCH q.asset a " +
            "LEFT JOIN FETCH a.category " +
            "LEFT JOIN FETCH a.status " +
            "LEFT JOIN FETCH a.employee e " +
            "LEFT JOIN FETCH e.department " +
            "WHERE q.qrCode = :qrCode")
    Optional<QrCode> findByQrCodeWithFullDetails(@Param("qrCode") String qrCode);
}