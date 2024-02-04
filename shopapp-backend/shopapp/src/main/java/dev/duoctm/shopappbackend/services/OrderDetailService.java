package dev.duoctm.shopappbackend.services;

import dev.duoctm.shopappbackend.dtos.OrderDetailDTO;
import dev.duoctm.shopappbackend.exceptions.DataNotFoundException;
import dev.duoctm.shopappbackend.models.Order;
import dev.duoctm.shopappbackend.models.OrderDetail;
import dev.duoctm.shopappbackend.models.Product;
import dev.duoctm.shopappbackend.repositories.OrderDetailRepository;
import dev.duoctm.shopappbackend.repositories.OrderRepository;
import dev.duoctm.shopappbackend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements  IOrderDetailService{
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException(
                        "Cant find order with id " + orderDetailDTO.getOrderId()
                ));

        Product product = productRepository.findById( orderDetailDTO.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Cant find product with id " + orderDetailDTO.getProductId()));


        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .color(orderDetailDTO.getColor())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .build();
        return orderDetailRepository.save(orderDetail);

    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws  Exception {

        return orderDetailRepository.findById(id)
                .orElseThrow(
                        ()-> new DataNotFoundException("Cant find order detail with id " + id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cant find order detail with id " + id));    ;

        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("Cant find order with id " + orderDetailDTO.getOrderId()));

        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Cant find product with id "+ orderDetailDTO.getProductId()));

        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);

        return orderDetailRepository.save(existingOrderDetail);

    }



    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findOrderById(orderId);
    }
}
