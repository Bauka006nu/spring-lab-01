package kz.iitu.springlab.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.stream.Collectors;

/** Puts "who is looking" into every Thymeleaf model (replaces sec:authentication). */
@ControllerAdvice
public class CurrentUserAdvice {

    private Authentication current() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return (a == null || a instanceof AnonymousAuthenticationToken || !a.isAuthenticated()) ? null : a;
    }

    @ModelAttribute("currentUser")
    public String currentUser() {
        Authentication a = current();
        return a == null ? null : a.getName();
    }

    @ModelAttribute("currentRoles")
    public String currentRoles() {
        Authentication a = current();
        return a == null ? "" : a.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(", "));
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Authentication a = current();
        return a != null && a.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
    }
}
