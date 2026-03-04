package com.example.app.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public class SecurityUtil {
    private SecurityUtil(){}
    public static Long getCurrentUserId(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        if (auth==null||!auth.isAuthenticated()||auth.getPrincipal()== null){
            return null;
        }
        if (auth.getPrincipal() instanceof CustomUserDetails cud){
            return cud.getId();
        }
        return null;
    }
}
