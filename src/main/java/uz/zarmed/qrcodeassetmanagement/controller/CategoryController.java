package uz.zarmed.qrcodeassetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.zarmed.qrcodeassetmanagement.dto.request.CategoryRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.CategoryResponseDto;
import uz.zarmed.qrcodeassetmanagement.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CategoryResponseDto>> getall(){
        List<CategoryResponseDto> list = categoryService.getAllCategorys();
        if(list.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryRequestDto dto){
        String currentUser="admin@company.com";
        return ResponseEntity.status(201).body(categoryService.createCategory(dto,currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> update (@PathVariable Long id, @RequestBody CategoryRequestDto dto){
        String currentUser="admin@company.com";
        return ResponseEntity.ok().body(categoryService.updateCategory(id,dto,currentUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        String currentUser="admin@company.com";
        categoryService.deleteCategory(id,currentUser);
        return ResponseEntity.noContent().build();
    }
}
