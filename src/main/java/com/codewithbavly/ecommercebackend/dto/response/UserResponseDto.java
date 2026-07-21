package com.codewithbavly.ecommercebackend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;
}