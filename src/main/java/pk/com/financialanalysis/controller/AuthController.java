package pk.com.financialanalysis.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import pk.com.financialanalysis.config.JwtResponse;
import pk.com.financialanalysis.config.JwtUtils;
import pk.com.financialanalysis.model.User;
import pk.com.financialanalysis.repository.UserRepository;
import pk.com.financialanalysis.service.UserDetailsServiceImpl;
import pk.com.financialanalysis.service.UserService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("signup")
    public ResponseEntity<String> signup(@Valid @RequestBody User user) {
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already Exist", HttpStatus.CONFLICT);
        }
        userService.registerUser(user);
        return new ResponseEntity<>("User registered sucessfully", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody User loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            // Generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String jwtToken = jwtUtils.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(jwtToken));
        } catch (BadCredentialsException ex) {
            // Handle wrong password scenario
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String username, @RequestParam String newPassword) {
        // Find the user by username
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Update the password
        user.setPassword(newPassword);
        userService.registerUser(user);

        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }


    @PostMapping("logout")
    public ResponseEntity<String> logout() {
        // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Clear the authentication
        SecurityContextHolder.clearContext();

        return new ResponseEntity<>("Logout successful", HttpStatus.OK);
    }


}

