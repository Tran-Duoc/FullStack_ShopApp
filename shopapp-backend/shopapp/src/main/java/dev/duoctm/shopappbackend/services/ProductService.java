package dev.duoctm.shopappbackend.services;

import dev.duoctm.shopappbackend.dtos.ProductDTO;
import dev.duoctm.shopappbackend.dtos.ProductImageDTO;
import dev.duoctm.shopappbackend.exceptions.DataNotFoundException;
import dev.duoctm.shopappbackend.exceptions.InvalidParamException;
import dev.duoctm.shopappbackend.models.Categogy;
import dev.duoctm.shopappbackend.models.Product;
import dev.duoctm.shopappbackend.models.ProductImage;
import dev.duoctm.shopappbackend.repositories.CategoryRepository;
import dev.duoctm.shopappbackend.repositories.ProductImageRepository;
import dev.duoctm.shopappbackend.repositories.ProductRepository;
import dev.duoctm.shopappbackend.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Categogy existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()->
                        new DataNotFoundException(
                                "Cant find category with id " + productDTO.getCategoryId())
                );
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .category(existingCategory)
                .description(productDTO.getDescription())
                 .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Product not found with id: " + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
         return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
}
    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        if(existingProduct != null) {
            Categogy existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(()->
                            new DataNotFoundException("Product not found with category id " + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());

            return  productRepository.save(existingProduct);
        }

        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()-> new DataNotFoundException("Cant find product with id " + productId ));

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw  new InvalidParamException("Size must be <= 5");

        }

        return  productImageRepository.save(newProductImage);
    }

}
