package com.example.app.order;
import com.example.app.order.dto.CreateOrderRequest;
import com.example.app.order.dto.OrderResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins="*")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service){
        this.service=service;
    }
    @PostMapping
    public OrderResponse create(@RequestBody CreateOrderRequest req){
        return service.create(req);
    }
    @GetMapping("/by-user/{userId}")
    public List<OrderResponse> getByUser(@PathVariable Long userId){
        return service.getByUser(userId);
    }
}
