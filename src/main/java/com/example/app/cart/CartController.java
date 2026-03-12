package com.example.app.cart;
import com.example.app.cart.dto.CartItemResponse;
import com.example.app.cart.dto.UpdateCartItemRequest;
import com.example.app.security.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins="*")
public class CartController {
    private final CartService service;
    public CartController(CartService service){
        this.service=service;
    }
    @GetMapping
    @PreAuthorize("hasAuthority('CART_READ_SELF')")
    public List<CartItemResponse> getMyCart(){
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        return service.getCartByUser(currentUserId);
    }
    @PutMapping
    @PreAuthorize("hasAuthority('CART_UPDATE_SELF')")
    public List<CartItemResponse> updateItem(@RequestBody UpdateCartItemRequest req){
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        return service.updateItem(currentUserId, req);
    }
    @DeleteMapping("/clear")
    @PreAuthorize("hasAuthority('CART_UPDATE_SELF')")
    public List<CartItemResponse> clearCart(){
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        return service.clearCart(currentUserId);
    }
}
