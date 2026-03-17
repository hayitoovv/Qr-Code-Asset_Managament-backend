package uz.zarmed.qrcodeassetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.zarmed.qrcodeassetmanagement.dto.request.EmployeeRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.EmployeeResponseDto;
import uz.zarmed.qrcodeassetmanagement.entity.Employee;
import uz.zarmed.qrcodeassetmanagement.repository.EmployeeRepository;
import uz.zarmed.qrcodeassetmanagement.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("permitAll()") // ALL
    public ResponseEntity<List<EmployeeResponseDto>> getAll() {
        List<EmployeeResponseDto> list = employeeService.getAllEmployees();
        if(list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()") // ALL
    public ResponseEntity<EmployeeResponseDto> getById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(employeeService.getEmplyeeById(id));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDto> create(@RequestBody EmployeeRequestDto dto) {
        String currentUser="admin@company.com";

        return ResponseEntity.status(201).body(employeeService.createEmployee(dto,currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDto> update(@PathVariable Long id,@RequestBody EmployeeRequestDto dto){
        String currentUser="admin@company.com";
        return ResponseEntity.ok(employeeService.updateEmployee(id,dto,currentUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        String currentUser="admin@company.com";
        employeeService.deleteEmploee(id,currentUser);
        return ResponseEntity.noContent().build();
    }
}
