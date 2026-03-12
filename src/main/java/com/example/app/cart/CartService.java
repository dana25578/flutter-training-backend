package com.example.app.cart;
import com.example.app.cart.dto.CartItemResponse;
import com.example.app.cart.dto.UpdateCartItemRequest;
import com.example.app.product.Product;
import com.example.app.product.ProductRepository;
import com.example.app.user.User;
import com.example.app.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class CartService {
    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    public CartService(CartItemRepository cartRepo,ProductRepository productRepo,UserRepository userRepo){
        this.cartRepo=cartRepo;
        this.productRepo=productRepo;
        this.userRepo=userRepo;
    }
    private CartItemResponse toResponse(CartItem item){
        CartItemResponse r=new CartItemResponse();
        r.productId=item.getProduct().getId();
        r.name=item.getProduct().getName();
        r.imageUrl=item.getProduct().getImageUrl();
        r.price=item.getProduct().getPrice();
        r.quantity=item.getQuantity();
        return r;
    }
    public List<CartItemResponse> getCartByUser(Long userId){
        return cartRepo.findByUserId(userId).stream().map(this::toResponse).toList();
    }
    @Transactional
    public List<CartItemResponse> updateItem(Long userId,UpdateCartItemRequest req){
        if (req==null) throw new RuntimeException("Request is missing");
        if (req.productId==null) throw new RuntimeException("productId is required");
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        Product product=productRepo.findById(req.productId).orElseThrow(()->new RuntimeException("Product not found"));
        var existingOpt=cartRepo.findByUserIdAndProductId(userId,req.productId);
        if (req.quantity<=0){
            existingOpt.ifPresent(cartRepo::delete);
            return getCartByUser(userId);
        }
        CartItem item=existingOpt.orElseGet(CartItem::new);
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(req.quantity);
        cartRepo.save(item);
        return getCartByUser(userId);
    }
    @Transactional
    public List<CartItemResponse> clearCart(Long userId){
        cartRepo.deleteByUserId(userId);
        return List.of();
    }
}