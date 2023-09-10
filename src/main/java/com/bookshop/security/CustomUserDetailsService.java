package com.bookshop.security;

import com.bookshop.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //userRepository.findByEmail(username).orElseThrow(
        //        () -> new RegistrationException("Can't find a user by username " + username)
        //);
        return null;
    }
}
