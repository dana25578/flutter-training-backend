package com.example.app.user;
import com.example.app.user.dto.UserResponse;
import com.example.app.user.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins="*")
public class UserController{
    private final UserService service;
    public UserController(UserService service){
        this.service =service;
    }
    @GetMapping
    public List<UserResponse> getAll(){
        return service.getAll();
    }
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id){
        return service.getById(id);
    }
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id,@Valid @RequestBody UserUpdateRequest req){
        return service.update(id,req);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
