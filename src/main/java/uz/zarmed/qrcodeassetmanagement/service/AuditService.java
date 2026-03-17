package uz.zarmed.qrcodeassetmanagement.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import uz.zarmed.qrcodeassetmanagement.entity.AuditLog;
import uz.zarmed.qrcodeassetmanagement.entity.Department;
import uz.zarmed.qrcodeassetmanagement.repository.AuditLogRepository;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final ObjectMapper objectMapper;
    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void logCreate(String entityType, Long entityId, Object entity, String performedBy) {
       try{
           AuditLog auditLog = AuditLog.builder()
                   .entityType(entityType)
                   .entityId(entityId)
                   .action("CREATE")
                   .performedBy(performedBy)
                   .performedAt(LocalDateTime.now())
                   .newValue(objectMapper.writeValueAsString(entity))
                   .build();

           auditLogRepository.save(auditLog);
           log.info("Audit log created :{} {} by {} ",entityType,entityId,performedBy);

       } catch (Exception e) {
           log.error("Failed to create audit log",e);
       }
    }

    public void logUpdate(String entityType, Long entityId, Object oldEntity, Object newentity, String permormedBy) {
        try{
            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action("UPDATE")
                    .performedBy(permormedBy)
                    .performedAt(LocalDateTime.now())
                    .oldValue(objectMapper.writeValueAsString(oldEntity))
                    .newValue(objectMapper.writeValueAsString(newentity))
                    .build();

            auditLogRepository.save(auditLog);
            log.info("Audit log updated: {} {} by {}",entityType,entityId,permormedBy);

        } catch (Exception e) {
            log.error("Failed to update audit log",e);
        }
    }

    @Transactional
    public void logDelete(String entityType, Long entityId, Object entity, String performedBy) {
        try{
            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action("DELETE")
                    .performedBy(performedBy)
                    .performedAt(LocalDateTime.now())
                    .oldValue(objectMapper.writeValueAsString(entity))
                    .build();

            auditLogRepository.save(auditLog);
            log.info("Audit log deleted: {} {} by {}" ,entityType,entityId,performedBy);

        }catch (Exception e){
            log.error("Failed to delete audit log",e);
        }
    }


    @Transactional
    public void logRestore(String entityType, Long entityId, Object entity, String performedBy) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action("RESTORE")
                    .performedBy(performedBy)
                    .performedAt(LocalDateTime.now())
                    .newValue(objectMapper.writeValueAsString(entity))
                    .build();

            auditLogRepository.save(auditLog);
            log.info("Audit log restored: {} {} by {}", entityType, entityId, performedBy);
        } catch (Exception e) {
            log.error("Failed to restore audit log", e);
        }
    }


}
