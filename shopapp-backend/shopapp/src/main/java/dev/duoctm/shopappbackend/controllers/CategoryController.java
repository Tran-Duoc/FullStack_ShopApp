package dev.duoctm.shopappbackend.controllers;

import dev.duoctm.shopappbackend.dtos.CategoryDTO;
import dev.duoctm.shopappbackend.models.Categogy;
import dev.duoctm.shopappbackend.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return  ResponseEntity.badRequest().body(errors);
        }

        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("insert category successfully");
    }

    @GetMapping("")
    public ResponseEntity<List<Categogy>> getAllCategories(
                @RequestParam("page") int page,
                @RequestParam("limit") int limit

    ){
        List<Categogy> categogies = categoryService.getAllCategories();
        return ResponseEntity.ok(categogies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
                @PathVariable Long id,
                @Valid @RequestBody CategoryDTO categoryDTO
    ){
    categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok( "Updated category successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with ID: " + id + " successfully");
    }
}
