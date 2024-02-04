package dev.duoctm.shopappbackend.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1,message = "Order's ID must be greater than 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1,message = "Product's ID must be greater than 0")
    private Long productId;

    @Min(value = 0,message = "Price of product must be greater or equal to 0")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1,message = "Number of product  must be greater than or equal to 1")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 1,message = "Total money must be greater than 0")
    private Float totalMoney;

    private String color;

}
