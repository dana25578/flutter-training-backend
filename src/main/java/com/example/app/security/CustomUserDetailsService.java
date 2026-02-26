package com.example.app.security;
import com.example.app.user.User;
import com.example.app.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    public CustomUserDetailsService(UserRepository repo){
        this.repo=repo;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user=repo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}

