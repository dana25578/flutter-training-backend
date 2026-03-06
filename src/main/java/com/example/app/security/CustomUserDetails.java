package com.example.app.security;
import com.example.app.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;
public class CustomUserDetails implements UserDetails{
    private final User user;
    public CustomUserDetails(User user){
        this.user =user;
    }
    public Long getId(){
        return user.getId();
    }
    public String getEmail(){
        return user.getEmail();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return user.getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getName())).collect(Collectors.toList());
    }
    @Override
    public String getPassword(){
        return user.getPasswordHash();
    }
    @Override
    public String getUsername(){
        return user.getEmail();
    }
    @Override
    public boolean isAccountNonExpired(){return true;}
    @Override
    public boolean isAccountNonLocked(){return true;}
    @Override
    public boolean isCredentialsNonExpired(){return true;}
    @Override
    public boolean isEnabled(){
        return user.isEnabled();
    }
}
