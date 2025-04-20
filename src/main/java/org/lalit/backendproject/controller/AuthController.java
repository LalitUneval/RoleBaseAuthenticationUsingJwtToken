package org.lalit.backendproject.controller;

import org.lalit.backendproject.DTOs.AuthRequest;
import org.lalit.backendproject.DTOs.AuthResponse;
import org.lalit.backendproject.model.Role;
import org.lalit.backendproject.model.User;
import org.lalit.backendproject.repository.RoleRepository;
import org.lalit.backendproject.repository.UserRepository;
import org.lalit.backendproject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/register")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private RoleRepository roleRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserDetailsService userDetailsService;
    @Autowired JwtUtil jwtUtil;

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepo.findByName("USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("USER");
            return roleRepo.save(r);
        });

        user.getRoles().add(userRole);
        userRepo.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User admin = new User();
        admin.setUsername(request.getUsername());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        Role adminRole = roleRepo.findByName("ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ADMIN");
            return roleRepo.save(r);
        });

        admin.getRoles().add(adminRole);
        userRepo.save(admin);

        return ResponseEntity.ok("Admin registered successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Load the actual user details with roles
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String  token = jwtUtil.generateToken(userDetails);
        System.out.println(token);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
