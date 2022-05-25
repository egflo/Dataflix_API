package com.moviedb_api.security;

import com.moviedb_api.customer.Customer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements AuthInterface {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public Boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
    }
    //Get the user's id
    public int getUserId() {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }
}
