package dev.duoctm.shopappbackend.services;
import dev.duoctm.shopappbackend.dtos.CategoryDTO;
import dev.duoctm.shopappbackend.models.Categogy;
import dev.duoctm.shopappbackend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Categogy createCategory(CategoryDTO categoryDTO) {
        Categogy newCategory = Categogy
                .builder()
                .name(categoryDTO.getName())
                .build();
        return  categoryRepository.save(newCategory);
    }


    @Override
    public Categogy getCategoryById(Long id) {
        return  categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<Categogy> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Categogy updateCategory(Long id, CategoryDTO categoryDTO) {
       Categogy existingCategory = getCategoryById(id);
       existingCategory.setName(categoryDTO.getName());
       categoryRepository.save(existingCategory);
       return existingCategory;

    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
