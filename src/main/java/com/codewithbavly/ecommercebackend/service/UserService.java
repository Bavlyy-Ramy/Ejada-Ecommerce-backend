package com.codewithbavly.ecommercebackend.service;

import com.codewithbavly.ecommercebackend.dto.request.LoginRequestDto;
import com.codewithbavly.ecommercebackend.dto.request.ProductRequestDto;
import com.codewithbavly.ecommercebackend.dto.request.UserRequestDto;
import com.codewithbavly.ecommercebackend.dto.response.LoginResponseDto;
import com.codewithbavly.ecommercebackend.dto.response.ProductResponseDto;
import com.codewithbavly.ecommercebackend.dto.response.UserResponseDto;
import com.codewithbavly.ecommercebackend.entity.Product;
import com.codewithbavly.ecommercebackend.entity.Role;
import com.codewithbavly.ecommercebackend.entity.User;
import com.codewithbavly.ecommercebackend.entity.enums.RoleName;
import com.codewithbavly.ecommercebackend.exception.DuplicateResourceException;
import com.codewithbavly.ecommercebackend.exception.ResourceNotFoundException;
import com.codewithbavly.ecommercebackend.repository.RoleRepository;
import com.codewithbavly.ecommercebackend.repository.UserRepository;
import com.codewithbavly.ecommercebackend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public UserResponseDto registerUser(UserRequestDto requestDto) {

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Default USER role not found"));

        User user = mapToEntity(requestDto);

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        return mapToResponseDto(savedUser);
    }


    public LoginResponseDto login(LoginRequestDto requestDto) {

        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return LoginResponseDto.builder()
                .token(token)
                .build();
    }



    // Mapping Methods
    private User mapToEntity(UserRequestDto dto) {

        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }
    private UserResponseDto mapToResponseDto(User user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}