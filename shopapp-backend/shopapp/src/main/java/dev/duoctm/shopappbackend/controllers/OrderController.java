package dev.duoctm.shopappbackend.controllers;


import dev.duoctm.shopappbackend.dtos.OrderDTO;
import dev.duoctm.shopappbackend.models.Order;
import dev.duoctm.shopappbackend.responses.OrderResponse;
import dev.duoctm.shopappbackend.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);

            }

            Order orderResponse = orderService.createOrder(orderDTO);

           return  ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        } catch(Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
        try {
            List<Order> ordersResponse = orderService.findByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(ordersResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId){
        try {
            Order orderResponse =  orderService.getOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrders(@Valid @PathVariable Long id,
                                          @RequestBody OrderDTO orderDTO){
        try {
            Order orderResponse = orderService.updateOrder(id, orderDTO);

            return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrders(@PathVariable Long id){
        try{
            // set isActive = false
            orderService.deleteOrder(id);
        return  ResponseEntity.status(HttpStatus.OK).body("delete order from id orders");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
