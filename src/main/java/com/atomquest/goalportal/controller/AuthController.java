package com.atomquest.goalportal.controller;

import com.atomquest.goalportal.dto.LoginRequest;
import com.atomquest.goalportal.entity.User;
import com.atomquest.goalportal.repository.UserRepository;
import com.atomquest.goalportal.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:5173","https://goalportal-frontend.vercel.app/"})
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())
        );
        return userRepository.save(user);
    }
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @PostMapping("/Login")
    public Map<String,String>login(
            @RequestBody LoginRequest request
            ) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        boolean valid = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );
        if(!valid){
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtService.generateToken(
                user.getEmail()
        );

        return Map.of(
                "token",token,
                "role",user.getRole(),
                "name",user.getName()
        );
    }
}
