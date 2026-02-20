package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.zarmed.qrcodeassetmanagement.entity.AuditLog;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}