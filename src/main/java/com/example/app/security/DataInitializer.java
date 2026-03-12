package com.example.app.security;
import com.example.app.user.User;
import com.example.app.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Set;
@Component
public class DataInitializer implements CommandLineRunner {
    private final PermissionRepository permRepo;
    private final UserRepository userRepo;
    public DataInitializer(PermissionRepository permRepo,UserRepository userRepo){
        this.permRepo=permRepo;
        this.userRepo=userRepo;
    }
    private Permission getOrCreate(String name){
        return permRepo.findByName(name).orElseGet(()->permRepo.save(new Permission(name)));
    }
    @Override
    public void run(String... args){
        Permission ORDER_CREATE=getOrCreate("ORDER_CREATE");
        Permission ORDER_READ_OWN=getOrCreate("ORDER_READ_OWN");
        Permission USER_READ_SELF=getOrCreate("USER_READ_SELF");
        Permission USER_UPDATE_SELF=getOrCreate("USER_UPDATE_SELF");
        Permission USER_READ_ALL=getOrCreate("USER_READ_ALL");
        Permission USER_DELETE=getOrCreate("USER_DELETE");
        Permission CART_READ_SELF=getOrCreate("CART_READ_SELF");
        Permission CART_UPDATE_SELF=getOrCreate("CART_UPDATE_SELF");
        Permission WISHLIST_READ_SELF=getOrCreate("WISHLIST_READ_SELF");
        Permission WISHLIST_UPDATE_SELF=getOrCreate("WISHLIST_UPDATE_SELF");
        for (User u:userRepo.findAll()){
            if (u.isEnabled()){
                u.getPermissions().addAll(Set.of(ORDER_CREATE,ORDER_READ_OWN,USER_READ_SELF,USER_UPDATE_SELF,CART_READ_SELF,CART_UPDATE_SELF,WISHLIST_READ_SELF,WISHLIST_UPDATE_SELF));
                if (u.getEmail()!=null && u.getEmail().equalsIgnoreCase("admin@admin.com")){
                    u.getPermissions().addAll(Set.of(USER_READ_ALL,USER_DELETE));
                }
                userRepo.save(u);
            }
        }
    }
}
