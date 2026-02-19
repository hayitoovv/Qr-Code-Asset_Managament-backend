package uz.zarmed.qrcodeassetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.zarmed.qrcodeassetmanagement.dto.request.EmployeeRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.EmployeeResponseDto;
import uz.zarmed.qrcodeassetmanagement.entity.Department;
import uz.zarmed.qrcodeassetmanagement.entity.Employee;
import uz.zarmed.qrcodeassetmanagement.exception.DepartmentNotFoundException;
import uz.zarmed.qrcodeassetmanagement.exception.EmailAlredyExistException;
import uz.zarmed.qrcodeassetmanagement.repository.DepartmentRepository;
import uz.zarmed.qrcodeassetmanagement.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // Get ALL
    public List<EmployeeResponseDto>  getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }


    // GET BY ID
    public EmployeeResponseDto getEmplyeeById(Long id) {
        Employee employee=employeeRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Employee not found: " + id));
        return toDto(employee);
    }




    // CREAT
    public EmployeeResponseDto createEmployee(EmployeeRequestDto dto) {
        if(employeeRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlredyExistException(dto.getEmail());
        }
      Department department= departmentRepository.findById(dto.getDepartmentId())
              .orElseThrow(  () -> new DepartmentNotFoundException(dto.getDepartmentId()));

            Employee employee = Employee.builder()
                    .fullName(dto.getFullName())
                    .phoneNumber(dto.getPhoneNumber())
                    .email(dto.getEmail())
                    .position(dto.getPosition())
                    .department(department)
                    .build();
        Employee saved = employeeRepository.save(employee);

        return toDto(saved);
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