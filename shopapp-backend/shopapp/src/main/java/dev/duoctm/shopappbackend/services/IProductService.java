package dev.duoctm.shopappbackend.services;

import dev.duoctm.shopappbackend.dtos.ProductDTO;
import dev.duoctm.shopappbackend.dtos.ProductImageDTO;
import dev.duoctm.shopappbackend.exceptions.DataNotFoundException;
import dev.duoctm.shopappbackend.models.Product;
import dev.duoctm.shopappbackend.models.ProductImage;
import dev.duoctm.shopappbackend.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(Long id) throws Exception;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id);

    boolean existsByName(String name);
    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws DataNotFoundException, Exception;

}
