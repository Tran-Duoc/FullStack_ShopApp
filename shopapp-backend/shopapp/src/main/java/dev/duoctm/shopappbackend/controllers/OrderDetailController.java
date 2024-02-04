package dev.duoctm.shopappbackend.controllers;

import dev.duoctm.shopappbackend.dtos.OrderDetailDTO;
import dev.duoctm.shopappbackend.models.OrderDetail;
import dev.duoctm.shopappbackend.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetails(@Valid @RequestBody OrderDetailDTO newOrderDetail){
        try {
            OrderDetail orderDetailResponse = orderDetailService.createOrderDetail(newOrderDetail);
            return ResponseEntity.status(HttpStatus.OK).body(orderDetailResponse);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable("id") Long id){
        try {
            orderDetailService.getOrderDetail(id);
            return ResponseEntity.status(HttpStatus.OK).body(" getting order details with " + id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getListOrderDetail(@Valid @PathVariable("orderId")Long orderId){
        List<OrderDetail> orderDetail = orderDetailService.findByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDetail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
                @Valid @PathVariable("id")Long id,
                @RequestBody OrderDetailDTO newOrderDetailData
            ) throws Exception {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, newOrderDetailData);
            return ResponseEntity.status(HttpStatus.OK).body(orderDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id")Long id){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok().body("Delete Order Detail successfully");
    }
}


