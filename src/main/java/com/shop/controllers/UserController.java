package com.shop.controllers;

import com.shop.dtos.UserDTO;
import com.shop.dtos.UserLoginDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.response.LoginResponse;
import com.shop.response.UserResponse;
import com.shop.services.UserService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) throws DataNotFoundException {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
            ).toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        try {
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Password and retype password must be same");
            }
            return ResponseEntity.ok(userService.createUser(userDTO));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String[] results = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            String token = results[0];
            int roleId = Integer.parseInt(results[1]);
            return ResponseEntity.ok(LoginResponse.builder()
                    .token(token)
                    .roleId(roleId)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult result
                                        , @PathVariable("id") Integer id,
                                        @RequestHeader("Authorization") String authHeader) throws DataNotFoundException {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Password and retype password must be same");
            }
            String extractedToken = authHeader.substring(7);
            UserResponse user = userService.getUserDetailsFromToken(extractedToken);
            if(user.getId() != id) {
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(userService.updateUser(userDTO,id));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        try {
            String extractedToken = authHeader.substring(7);
            UserResponse user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Implement other methods for login, update, and delete (consider security)
    // ...
}