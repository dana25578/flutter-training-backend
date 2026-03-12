package com.example.app.wishlist;
import com.example.app.product.Product;
import com.example.app.product.ProductRepository;
import com.example.app.user.User;
import com.example.app.user.UserRepository;
import com.example.app.wishlist.dto.ToggleWishlistRequest;
import com.example.app.wishlist.dto.WishlistItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class WishlistService {
    private final WishlistItemRepository wishlistRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    public WishlistService(WishlistItemRepository wishlistRepo,ProductRepository productRepo,UserRepository userRepo){
        this.wishlistRepo=wishlistRepo;
        this.productRepo=productRepo;
        this.userRepo=userRepo;
    }
    private WishlistItemResponse toResponse(WishlistItem item){
        WishlistItemResponse r=new WishlistItemResponse();
        r.productId=item.getProduct().getId();
        r.name=item.getProduct().getName();
        r.description=item.getProduct().getDescription();
        r.price=item.getProduct().getPrice();
        r.imageUrl=item.getProduct().getImageUrl();
        r.categoryId=item.getProduct().getCategory().getId();
        return r;
    }
    public List<WishlistItemResponse> getWishlistByUser(Long userId){
        return wishlistRepo.findByUserId(userId).stream().map(this::toResponse).toList();
    }
    @Transactional
    public List<WishlistItemResponse> toggle(Long userId,ToggleWishlistRequest req){
        if (req==null||req.productId==null){
            throw new RuntimeException("productId is required");
        }
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("user not found"));
        Product product=productRepo.findById(req.productId).orElseThrow(()->new RuntimeException("product not found"));
        var existing=wishlistRepo.findByUserIdAndProductId(userId,req.productId);
        if (existing.isPresent()){
            wishlistRepo.delete(existing.get());
        }else{
            WishlistItem item=new WishlistItem();
            item.setUser(user);
            item.setProduct(product);
            wishlistRepo.save(item);
        }
        return getWishlistByUser(userId);
    }
    @Transactional
    public List<WishlistItemResponse> clear(Long userId){
        wishlistRepo.deleteByUserId(userId);
        return List.of();
    }
}