package uz.zarmed.qrcodeassetmanagement.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.zarmed.qrcodeassetmanagement.dto.request.DepartmentRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.DepartmentResponseDto;
import uz.zarmed.qrcodeassetmanagement.entity.Department;
import uz.zarmed.qrcodeassetmanagement.entity.Employee;
import uz.zarmed.qrcodeassetmanagement.exception.BusinessException;
import uz.zarmed.qrcodeassetmanagement.exception.ErrorCode;
import uz.zarmed.qrcodeassetmanagement.exception.ResourceNotFoundException;
import uz.zarmed.qrcodeassetmanagement.repository.AssetRepository;
import uz.zarmed.qrcodeassetmanagement.repository.DepartmentRepository;
import uz.zarmed.qrcodeassetmanagement.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;
    private final EmployeeRepository employeeRepository;
    private final AssetRepository assetRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"+id));
        return toDto(department);
    }

    // CREATE
    @Transactional
    public DepartmentResponseDto createDepartment(DepartmentRequestDto dto, String createdBy ) {
        if(departmentRepository.existsByDepartmentCode(dto.getDepartmentCode())){
          throw new BusinessException(
                  ErrorCode.DEPARTMENT_CODE_ALREADY_EXISTS,
                 String.format( "Department code: '%s' alredy exsist",dto.getDepartmentCode())
          );
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
                .orElseThrow(() -> new ResourceNotFoundException("Department not found "+id));

        if (!department.getDepartmentCode().equals(dto.getDepartmentCode())) {
            // Code o'zgardi - boshqalarda bormi tekshirish kerak
            if (departmentRepository.existsByDepartmentCode(dto.getDepartmentCode())) {
                throw new BusinessException(
                        ErrorCode.DEPARTMENT_CODE_ALREADY_EXISTS,
                        String.format("Department code '%s' already exists. Please use a different code.",
                                dto.getDepartmentCode())
                );
            }
        }


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

    // DELETE - soft delete with validation
    @Transactional
    public void deleteDepartment(Long id,String deletedBy) {
        //1. Fetch entity
        Department department = departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found exception"+id));

        //2. Validation -Active employee bomri?
        Long activeEmployee = employeeRepository.countByDepartmentIdAndIsDeletedFalse(id);

        if(activeEmployee>0){
            throw new BusinessException(
                    ErrorCode.DEPARTMENT_HAS_EMPLOYEES,
                    String.format("Cannot  delete department '%s':'%d' active employee(s) still assigned."+
                            "Please reassign or remove employee first",
                    department.getDepartmentName(),activeEmployee)
            );

        }

        //3. Validation  -Active employeeler orqali assetlar bormi ?

      List<Long> employeeIds= employeeRepository.findByDepartmentIdAndIsDeletedFalse(id)
              .stream()
              .map(Employee::getId)
              .toList();
        long assetCount=0;
        for (Long empId : employeeIds) {
            assetCount+=assetRepository.countByEmployeeIdAndIsDeletedFalse(empId);
        }
        if(assetCount>0){
            throw new BusinessException(
                    ErrorCode.DEPARTMENT_HAS_ASSETS,
                    String.format("Cannot delete Department '%s' :'%d'asset(s) assigned to employees in this department",
                            department.getDepartmentName(),
                            assetCount)
            );
        }

        //4. Audit Log yozish
        auditService.logDelete("Department",id,department,deletedBy);

        //5.soft delete

        department.setIsDeleted(true);
        department.setDeletedAt(LocalDateTime.now());
        department.setDeletedBy(deletedBy);
        departmentRepository.save(department);

        log.info("Department soft deleted: {} by {}",id,deletedBy);

    }

    // Entity → DTO  // DTO convertor
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