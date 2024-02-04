package dev.duoctm.shopappbackend.responses;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListResponse {
    private int totalPages;
    private List<ProductResponse> products;
}
