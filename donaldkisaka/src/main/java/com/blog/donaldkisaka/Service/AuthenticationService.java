package com.blog.donaldkisaka.Service;

import com.blog.donaldkisaka.dto.LoginUser;
import com.blog.donaldkisaka.dto.RegisterUser;
import com.blog.donaldkisaka.model.Role;
import com.blog.donaldkisaka.model.User;
import com.blog.donaldkisaka.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signUp(RegisterUser input) {


        if (userRepository.existsByEmail(input.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (userRepository.existsByUsername(input.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        try {
            User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
            user.setEnabled(true);
            user.setRole(Role.USER);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Registration failed.");
        }
    }

    public User authenticate(LoginUser input) {
        try {
            User user = userRepository.findByEmail(input.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
            return user;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
