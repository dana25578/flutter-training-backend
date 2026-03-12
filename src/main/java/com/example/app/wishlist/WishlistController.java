package com.example.app.wishlist;
import com.example.app.security.SecurityUtil;
import com.example.app.wishlist.dto.ToggleWishlistRequest;
import com.example.app.wishlist.dto.WishlistItemResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins="*")
public class WishlistController {
    private final WishlistService service;
    public WishlistController(WishlistService service){
        this.service=service;
    }
    @GetMapping
    @PreAuthorize("hasAuthority('WISHLIST_READ_SELF')")
    public List<WishlistItemResponse> getMyWishlist() {
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        return service.getWishlistByUser(currentUserId);
    }
    @PutMapping("/toggle")
    @PreAuthorize("hasAuthority('WISHLIST_UPDATE_SELF')")
    public List<WishlistItemResponse> toggle(@RequestBody ToggleWishlistRequest req){
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        return service.toggle(currentUserId,req);
    }
    @DeleteMapping("/clear")
    @PreAuthorize("hasAuthority('WISHLIST_UPDATE_SELF')")
    public List<WishlistItemResponse> clear(){
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        return service.clear(currentUserId);
    }
}