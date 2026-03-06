package com.example.app.user;
import com.example.app.user.dto.UserResponse;
import com.example.app.user.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.app.security.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins="*")
public class UserController{
    private final UserService service;
    public UserController(UserService service){
        this.service =service;
    }
    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ_ALL')")
    public List<UserResponse> getAll(){
        return service.getAll();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ_SELF')")
    public UserResponse getById(@PathVariable Long id){
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        if (!id.equals(currentUserId)){
            throw new RuntimeException("Forbidden");
        }
        return service.getById(id);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE_SELF')")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req){
        Long currentUserId=SecurityUtil.getCurrentUserId();
        if (currentUserId==null){
            throw new RuntimeException("unauthorized");
        }
        if (!id.equals(currentUserId)){
            throw new RuntimeException("Forbidden");
        }
        return service.update(id,req);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
