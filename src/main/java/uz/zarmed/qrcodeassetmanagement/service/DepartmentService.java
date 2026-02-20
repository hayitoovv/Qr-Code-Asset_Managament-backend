package uz.zarmed.qrcodeassetmanagement.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.zarmed.qrcodeassetmanagement.dto.request.DepartmentRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.DepartmentResponseDto;
import uz.zarmed.qrcodeassetmanagement.entity.Department;
import uz.zarmed.qrcodeassetmanagement.exception.DepartmentCodeAlredyExistException;
import uz.zarmed.qrcodeassetmanagement.exception.DepartmentNotFoundException;
import uz.zarmed.qrcodeassetmanagement.repository.DepartmentRepository;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;

    // GET ALL
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findByIsDeletedFalse()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // GET BY ID
    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
        return toDto(department);
    }

    // CREATE
    @Transactional
    public DepartmentResponseDto createDepartment(DepartmentRequestDto dto, String createdBy ) {
        if(departmentRepository.existsByDepartmentCode(dto.getDepartmentCode())){
            throw new DepartmentCodeAlredyExistException(dto.getDepartmentCode());
        }
        Department department = Department.builder()
                .departmentName(dto.getDepartmentName())
                .departmentCode(dto.getDepartmentCode())
                .departmentDescription(dto.getDepartmentDescription())
                .build();

        Department saved = departmentRepository.save(department);
        //Audit log
        auditService.logCreate("Department", saved.getId(), saved, createdBy );
        log.info("Audit log created :{} by {} ",saved.getId(),createdBy);
        return toDto(saved);
    }

    // UPDATE
    @Transactional
    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto dto ,String updatedBy) {
        Department department = departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));


        //old value for audit
        Department oldDepatment = Department.builder()
                .departmentName(department.getDepartmentName())
                .departmentCode(department.getDepartmentCode())
                .departmentDescription(department.getDepartmentDescription())
                .build();


        //update
        department.setDepartmentName(dto.getDepartmentName());
        department.setDepartmentCode(dto.getDepartmentCode());
        department.setDepartmentDescription(dto.getDepartmentDescription());

        Department updated = departmentRepository.save(department);

        //Audit log
        auditService.logUpdate("Department",id,oldDepatment,updated,updatedBy);

        log.info("Department updated: {} by {}",id,updatedBy);
        return toDto(updated);
    }

    // DELETE
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found: " + id);
        }
        departmentRepository.deleteById(id);
    }

    // Entity → DTO   DTO convertor
    private DepartmentResponseDto toDto(Department department) {
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName())
                .departmentCode(department.getDepartmentCode())
                .departmentDescription(department.getDepartmentDescription())
                .createdAt(department.getCreatedAt())
                .build();
    }
}