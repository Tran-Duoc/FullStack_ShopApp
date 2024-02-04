package dev.duoctm.shopappbackend.services;
import dev.duoctm.shopappbackend.dtos.CategoryDTO;
import dev.duoctm.shopappbackend.models.Categogy;

import java.util.List;

public interface ICategoryService {
    Categogy createCategory (CategoryDTO category);

    Categogy getCategoryById (Long id);

    List<Categogy> getAllCategories ();

    Categogy updateCategory (Long id, CategoryDTO category);

    void deleteCategory (Long id);
}
