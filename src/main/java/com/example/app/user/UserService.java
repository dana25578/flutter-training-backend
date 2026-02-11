package com.example.app.user;
import com.example.app.user.dto.UserResponse;
import com.example.app.user.dto.UserUpdateRequest;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo){
        this.repo =repo;
    }
    private UserResponse toResponse(User u){
        UserResponse r =new UserResponse();
        r.id =u.getId();
        r.username=u.getUsername();
        r.email=u.getEmail();
        r.enabled=u.isEnabled();
        r.createdAt=u.getCreatedAt();
        return r;
    }
    public List<UserResponse>getAll(){
        return repo.findAll().stream().map(this::toResponse).toList();
    }
    public UserResponse getById(Long id){
        User u =repo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        return toResponse(u);
    }
    public UserResponse update(Long id, UserUpdateRequest req){
        User u =repo.findById(id).orElseThrow(() ->new RuntimeException("User not found"));
        if (req.username !=null && !req.username.equals(u.getUsername())) {
            if (repo.existsByUsername(req.username)) throw new RuntimeException("Username already used");
            u.setUsername(req.username);
        }
        if (req.email !=null && !req.email.equals(u.getEmail())){
            if (repo.existsByEmail(req.email)) throw new RuntimeException("Email already used");
            u.setEmail(req.email);
        }
        if (req.enabled !=null) u.setEnabled(req.enabled);
        return toResponse(repo.save(u));
    }
    public void delete(Long id){
        if (!repo.existsById(id)) throw new RuntimeException("User not found");
        repo.deleteById(id);
    }
}
