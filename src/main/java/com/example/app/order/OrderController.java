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
        Long currentUserId= com.example.app.security.SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        if (req.userId==null||!req.userId.equals(currentUserId)){
            throw new RuntimeException("Forbidden: userId does not match token user");
        }
        return service.create(req);
    }
    @GetMapping("/by-user/{userId}")
    public List<OrderResponse> getByUser(@PathVariable Long userId){
        return service.getByUser(userId);
    }
}
