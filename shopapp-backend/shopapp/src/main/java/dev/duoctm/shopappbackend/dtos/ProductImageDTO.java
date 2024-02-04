package dev.duoctm.shopappbackend.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {

    @Min(value = 1, message = "Product's id must be greater than 1")
    @JsonProperty("product_id")
    private  Long productId;

    @Size(min =  5 , max = 200, message = "Name must be between 5 and 200 characters")
    @JsonProperty("image_url")
    private String imageUrl;

}
