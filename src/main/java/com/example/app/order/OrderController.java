package com.example.app.order;
import com.example.app.order.dto.CreateOrderRequest;
import com.example.app.order.dto.OrderResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.app.security.SecurityUtil;
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins="*")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service){
        this.service=service;
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    public OrderResponse create(@RequestBody CreateOrderRequest req){
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        if (req.userId==null||!req.userId.equals(currentUserId)){
            throw new RuntimeException("Forbidden: userId does not match token user");
        }
        return service.create(req);
    }
    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasAuthority('ORDER_READ_OWN')")
    public List<OrderResponse> getByUser(@PathVariable Long userId){
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        if (!userId.equals(currentUserId)){
        throw new RuntimeException("Forbidden: cannot read other user's orders");
        }
        return service.getByUser(userId);
    }
}
