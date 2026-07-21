package com.codewithbavly.ecommercebackend.controller;

import com.codewithbavly.ecommercebackend.dto.request.LoginRequestDto;
import com.codewithbavly.ecommercebackend.dto.request.UserRequestDto;
import com.codewithbavly.ecommercebackend.dto.response.LoginResponseDto;
import com.codewithbavly.ecommercebackend.dto.response.UserResponseDto;
import com.codewithbavly.ecommercebackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto requestDto) {UserResponseDto response = userService.registerUser(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {

        LoginResponseDto response = userService.login(requestDto);

        return ResponseEntity.ok(response);
    }



}