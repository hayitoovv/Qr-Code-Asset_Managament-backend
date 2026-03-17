package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zarmed.qrcodeassetmanagement.entity.Asset;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {


    // Basic queries
    List<Asset> findByIsDeletedFalse();
    Optional<Asset> findByIdAndIsDeletedFalse(Long id);

    // Validation
    boolean existsBySerialNumber(String serialNumber);
    boolean existsBySerialNumberAndIdNot(String serialNumber, Long id);

    // Count queries
    long countByEmployeeIdAndIsDeletedFalse(Long employeeId);
    long countByCategoryIdAndIsDeletedFalse(Long categoryId);
    long countByStatusIdAndIsDeletedFalse(Long statusId);

    // Filter queries
    List<Asset> findByStatusIdAndIsDeletedFalse(Long statusId);
    List<Asset> findByCategoryIdAndIsDeletedFalse(Long categoryId);
    List<Asset> findByEmployeeIdAndIsDeletedFalse(Long employeeId);

    // Advanced filter
    @Query("SELECT a FROM Asset a WHERE a.isDeleted = false " +
            "AND (:statusId IS NULL OR a.status.id = :statusId) " +
            "AND (:categoryId IS NULL OR a.category.id = :categoryId) " +
            "AND (:employeeId IS NULL OR a.employee.id = :employeeId)")
    List<Asset> findByFilters(
            @Param("statusId") Long statusId,
            @Param("categoryId") Long categoryId,
            @Param("employeeId") Long employeeId
    );

    // Search
    @Query("SELECT a FROM Asset a WHERE a.isDeleted = false " +
            "AND (LOWER(a.assetName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.serialNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Asset> searchAssets(@Param("searchTerm") String searchTerm);

    long countByIsDeletedFalse();
}