package uz.zarmed.qrcodeassetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<EmployeeResponseDto>> getAll() {
        List<EmployeeResponseDto> list = employeeService.getAllEmployees();
        if(list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(employeeService.getEmplyeeById(id));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> create(@RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto created = employeeService.createEmployee(dto);
        if(created == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(created);
    }



}
