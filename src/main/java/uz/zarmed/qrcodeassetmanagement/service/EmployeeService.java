package uz.zarmed.qrcodeassetmanagement.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.zarmed.qrcodeassetmanagement.dto.request.EmployeeRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.EmployeeResponseDto;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;
    private final AssetRepository assetRepository;

    // Get ALL
    public List<EmployeeResponseDto>  getAllEmployees() {
        return employeeRepository.findByIsDeletedFalse()
                .stream()
                .map(this::toDto)
                .toList();
    }


    // GET BY ID
    public EmployeeResponseDto getEmplyeeById(Long id) {
        Employee employee=employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        return toDto(employee);
    }




    // CREAT
    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto dto,String createdBy) {
        if(employeeRepository.existsByEmail(dto.getEmail())){
           throw  new BusinessException(
                   ErrorCode.EMPLOYEE_EMAIL_ALREDY_EXSIST,
                   String.format("Email alredi exsist:'%s'",dto.getEmail())
           );
        }

      Department department= departmentRepository.findByIdAndIsDeletedFalse(dto.getDepartmentId())
              .orElseThrow(  () -> new ResourceNotFoundException("Department not found exception "+dto.getDepartmentId()));

            Employee employee = Employee.builder()
                    .fullName(dto.getFullName())
                    .phoneNumber(dto.getPhoneNumber())
                    .email(dto.getEmail())
                    .position(dto.getPosition())
                    .department(department)
                    .build();

        Employee saved = employeeRepository.save(employee);

        //Audit log
        auditService.logCreate("Employee", saved.getId(), saved,createdBy);
        log.info("Audit log created:{} by{}",saved.getId(),createdBy);
        return toDto(saved);
    }



    //Update
    @Transactional
    public EmployeeResponseDto updateEmployee(Long employeeId, EmployeeRequestDto dto, String updatedBy) {

        Optional<Department> optionalDepartment = departmentRepository.findByIdAndIsDeletedFalse(dto.getDepartmentId());
        if(optionalDepartment.isEmpty()){
            throw new ResourceNotFoundException("Department not found with :"+dto.getDepartmentId());
        }
        Department department = optionalDepartment.get();

        Employee employee = employeeRepository.findById(employeeId).
                orElseThrow(() -> new ResourceNotFoundException("Employee not found with id:" + employeeId));

        if(!employee.getEmail().equals(dto.getEmail())){

            //boshqa employee email larini ham tekshirib koramiz
            if(employeeRepository.existsByEmail(dto.getEmail())){
                throw new BusinessException(
                        ErrorCode.EMPLOYEE_EMAIL_ALREDY_EXSIST,
                        String.format("Email alredy exsist:'%s'",dto.getEmail())
                );
            }
        }

        //old value for audit

        Employee oldEmployee = Employee.builder()
                .email(employee.getEmail())
                .fullName(employee.getFullName())
                .department(department)
                .phoneNumber(employee.getPhoneNumber())
                .position(employee.getPosition())
                .build();
        //update

        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDepartment(department);
        employee.setPosition(dto.getPosition());

        Employee newEmploee = employeeRepository.save(employee);

        //Audit log
        auditService.logUpdate("Employee",employeeId,oldEmployee,newEmploee,updatedBy);

        log.info("Employee updated:{} by{}",employeeId,updatedBy);
        return toDto(newEmploee);
    }

    // DELETE - soft delete with validation
    @Transactional
    public void deleteEmploee(Long id, String deletedBy) {
       //Fetch entity
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(" Employee Not found id:" + id));

        //Validation -Active asset bomri?
        long activeAssets = assetRepository.countByEmployeeIdAndIsDeletedFalse(id);

        if(activeAssets>0){
            throw new BusinessException(
                    ErrorCode.EMPLOYEE_HAS_ASSET,
                    String.format("Cannot  delete employee '%s':'%d' active asset(s) still assigned."+
                            "Please reassign or remove asset(s) first",employee.getFullName(),activeAssets)
            );
        }

        // Audit log yozish

        auditService.logDelete("Employee",id,employee,deletedBy);

        //Soft delete
        employee.setIsDeleted(true);
        employee.setDeletedAt(LocalDateTime.now());
        employee.setDeletedBy(deletedBy);
        employeeRepository.save(employee);

        log.info("Departmen deleted:{} by {}",id,deletedBy);

    }

    //Entity → Dto
    private EmployeeResponseDto toDto(Employee employee) {
        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .phoneNumber(employee.getPhoneNumber())
                .email(employee.getEmail())
                .position(employee.getPosition())
                .departmentId(employee.getDepartment().getId())
                .createdAt(employee.getCreatedAt())
                .build();

    }


}