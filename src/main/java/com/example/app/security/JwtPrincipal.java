package com.example.app.security;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
public class JwtPrincipal {
    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    public JwtPrincipal(Long id,String email,Collection<? extends GrantedAuthority> authorities){
        this.id=id;
        this.email=email;
        this.authorities=authorities;
    }
    public Long getId(){return id;}
    public String getEmail(){return email;}
    public Collection<? extends GrantedAuthority> getAuthorities(){return authorities;}
}
