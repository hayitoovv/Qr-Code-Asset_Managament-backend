package uz.zarmed.qrcodeassetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.zarmed.qrcodeassetmanagement.dto.request.DepartmentRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.DepartmentResponseDto;
import uz.zarmed.qrcodeassetmanagement.entity.Department;
import uz.zarmed.qrcodeassetmanagement.exception.DepartmentCodeAlredyExistException;
import uz.zarmed.qrcodeassetmanagement.repository.DepartmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    // GET ALL
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // GET BY ID
    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));
        return toDto(department);
    }

    // CREATE
    public DepartmentResponseDto createDepartment(DepartmentRequestDto dto) {
        if(departmentRepository.existsByDepartmentCode(dto.getDepartmentCode())){
            throw new DepartmentCodeAlredyExistException(dto.getDepartmentCode());
        }
        Department department = Department.builder()
                .departmentName(dto.getDepartmentName())
                .departmentCode(dto.getDepartmentCode())
                .departmentDescription(dto.getDepartmentDescription())
                .build();
        return toDto(departmentRepository.save(department));
    }

    // UPDATE
    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));

        department.setDepartmentName(dto.getDepartmentName());
        department.setDepartmentCode(dto.getDepartmentCode());
        department.setDepartmentDescription(dto.getDepartmentDescription());

        return toDto(departmentRepository.save(department));
    }

    // DELETE
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found: " + id);
        }
        departmentRepository.deleteById(id);
    }

    // Entity → DTO
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