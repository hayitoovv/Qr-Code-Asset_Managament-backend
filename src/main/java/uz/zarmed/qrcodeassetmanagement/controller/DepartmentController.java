package uz.zarmed.qrcodeassetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.zarmed.qrcodeassetmanagement.dto.request.DepartmentRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.DepartmentResponseDto;
import uz.zarmed.qrcodeassetmanagement.entity.Department;
import uz.zarmed.qrcodeassetmanagement.repository.DepartmentRepository;
import uz.zarmed.qrcodeassetmanagement.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    // GET /api/departments
    //GET - barcha departmentlar faqat active
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<DepartmentResponseDto>> getAll() {
        List<DepartmentResponseDto> list = departmentService.getAllDepartments();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    // GET /api/departments/id
    //GEt- bitta department
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<DepartmentResponseDto> getById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(departmentService.getDepartmentById(id));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDto> create(@RequestBody DepartmentRequestDto dto) {
        //soon add security
        String currentUser="admin@company.com";

        return ResponseEntity.status(201).body(departmentService.createDepartment(dto,currentUser));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDto> update(@PathVariable Long id,@RequestBody DepartmentRequestDto dto){
        String currentUser="admin@company.com";
        return ResponseEntity.ok(departmentService.updateDepartment(id,dto,currentUser));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        String currentUser="admin@company.com";
        departmentService.deleteDepartment(id,currentUser);
        return ResponseEntity.noContent().build();
    }
}




