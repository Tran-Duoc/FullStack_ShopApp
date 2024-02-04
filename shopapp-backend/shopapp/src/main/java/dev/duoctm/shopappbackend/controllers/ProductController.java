package dev.duoctm.shopappbackend.controllers;

import com.github.javafaker.Faker;
import dev.duoctm.shopappbackend.dtos.ProductDTO;
import dev.duoctm.shopappbackend.dtos.ProductImageDTO;
import dev.duoctm.shopappbackend.exceptions.DataNotFoundException;
import dev.duoctm.shopappbackend.models.Product;
import dev.duoctm.shopappbackend.models.ProductImage;
import dev.duoctm.shopappbackend.responses.ProductListResponse;
import dev.duoctm.shopappbackend.responses.ProductResponse;
import dev.duoctm.shopappbackend.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProduct(@RequestParam("page") int page,
                                                                @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending());

        Page<ProductResponse> productsPage = productService.getAllProducts(pageRequest);
        int totalPages = productsPage.getTotalPages();
        List<ProductResponse> products =productsPage.getContent();
        return  ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public  ResponseEntity<?> getProductById(@PathVariable("id") Long productId){
        try {
            Product existingProduct = productService.getProductById(productId);
            return  ResponseEntity.ok( ProductResponse.fromProduct(existingProduct));

        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }



    }
    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result) throws Exception {
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return  ResponseEntity.badRequest().body(errors);
        }

        Product newProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable Long id,
            @Valid @ModelAttribute("files")  List<MultipartFile> files
    ){

        try {
            Product existingProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return  ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();

            for(MultipartFile file : files){
                if(file.getSize() == 0) continue;
                if(file.getSize() > 10 * 1024 * 1024){ // file > 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body( "File is too large. Maximum size is 10MB");
                }

                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body( "File must be an image");
                }

                String filename = storeFile(file);
                ProductImage productImage =    productService.createProductImage(
                         existingProduct.getId(),
                        ProductImageDTO
                                .builder()
                                .productId(existingProduct.getId())
                                .imageUrl(filename)
                                .build());
                productImages.add(productImage);
            }


            return  ResponseEntity.ok(productImages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String storeFile(MultipartFile file) throws IOException {
            if(!isImageFile(file) || file.getOriginalFilename() == null) {

                throw  new IOException("Invalid file");
            }
            String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;

            Path uploadDir = Paths.get("uploads");

            if(!Files.exists(uploadDir)){
                Files.createDirectories(uploadDir);
            }

            Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFilename;
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id,
                                                @Valid @RequestBody ProductDTO productDTO
    ){
        try {
            productService.updateProduct(id, productDTO);
            return  ResponseEntity.status(HttpStatus.CREATED).body("update product successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String>  deleteProduct(@PathVariable Long id){
        try {
            productService.deleteProduct(id);
            return  ResponseEntity.status(HttpStatus.CREATED).body("delete product successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


//    @PostMapping("/generateFakeProducts")
    private ResponseEntity<?> generateFakeProducts(){
        Faker faker = new Faker();
        for (int i = 0; i < 1000000000; i++) {
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }

            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10, 90000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(2, 5))
                    .thumbnail("")
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (DataNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return  ResponseEntity.ok("Faker product created successfully");
    }
}
