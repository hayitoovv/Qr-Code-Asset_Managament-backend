package uz.zarmed.qrcodeassetmanagement.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.zarmed.qrcodeassetmanagement.dto.request.CategoryRequestDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.CategoryResponseDto;
import uz.zarmed.qrcodeassetmanagement.entity.Category;
import uz.zarmed.qrcodeassetmanagement.exception.BusinessException;
import uz.zarmed.qrcodeassetmanagement.exception.ErrorCode;
import uz.zarmed.qrcodeassetmanagement.exception.ResourceNotFoundException;
import uz.zarmed.qrcodeassetmanagement.repository.AssetRepository;
import uz.zarmed.qrcodeassetmanagement.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuditService auditService;
    private final AssetRepository assetRepository;

    //GET ALL CATEGORY
    public List<CategoryResponseDto> getAllCategorys() {
        return categoryRepository.findByIsDeletedFalse()
                .stream()
                .map(this::toDto)
                .toList();
    }


    public CategoryResponseDto getCategoryById(Long id){
       Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
               .orElseThrow(()->new ResourceNotFoundException("Category not found with id:"+id));
       return toDto(category);
    }


    //CREATE CATEGORY
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto dto, String createdBy) {
        if ( categoryRepository.existsByName(dto.getName())) {
           throw new BusinessException(
                   ErrorCode.CATEGORY_NAME_ALLREDY_EXSIST,
                   String.format("Category name alredy exsist: '%s'",dto.getName())
           );
        }

        Category category = Category.builder()
                .name(dto.getName())
                .icon(dto.getName())
                .build();

        Category saved = categoryRepository.save(category);

        //Audit log

        auditService.logCreate("Category", saved.getId(), saved,createdBy);
        log.info("Audit log created : {} by {}", saved.getId(),createdBy);
        return toDto(saved);
    }






    //Entity -> DTO convertor
    public CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .name(category.getName())
                .id(category.getId())
                .icon(category.getIcon())
                .createdAt(category.getCreatedAt())
                .build();
    }

    //UPDATE CATEGORY
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto, String updatedBy) {
        Category category=categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + id
                        )
                );

        if (!category.equals(dto.getName())) {
            //boshqalarini ham tekshiramiz
            if(categoryRepository.existsByName(dto.getName())){
                throw  new BusinessException(
                        ErrorCode.CATEGORY_NAME_ALLREDY_EXSIST,
                        String.format("Category name alredy exsist: '%s'",dto.getName())
                );
            }
        }

        //old valu for audit log
        Category oldcategory = Category.builder()
                .name(category.getName())
                .icon(category.getIcon())
                .build();

        //update
        category.setName(dto.getName());
        category.setIcon(dto.getName());

        Category updated = categoryRepository.save(category);

        //Audit log

        auditService.logUpdate("Category",id,oldcategory,updated,updatedBy);

        log.info("Category updated:{} by {}",id, updatedBy);

        return toDto(updated);

    }



    //Delete soft delete
    @Transactional
    public void deleteCategory(Long id, String deletedBy) {
      Category category= categoryRepository.findByIdAndIsDeletedFalse(id)
              .orElseThrow(()->new ResourceNotFoundException("Category not found id:"+id));

        //validation active assets bormi ?

       Long activeAssets=assetRepository.countByCategoryIdAndIsDeletedFalse(id);

       if(activeAssets>0){
           throw new BusinessException(
                   ErrorCode.ASSETS_ASSIGNED_CATEGORY,
                   String.format("Cannot  delete category '%s':'%d' active assets(s) still assigned."+
                                   "Please reassign or remove assets first",
                           category.getName(),activeAssets)
           );
       }

       //Audit log yozish

        auditService.logDelete("Category",id,category,deletedBy);

       //soft delete
        category.setIsDeleted(true);
        category.setDeletedBy(deletedBy);
        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);

        log.info("Category delted:{} by {}",id,deletedBy);

    }


}
