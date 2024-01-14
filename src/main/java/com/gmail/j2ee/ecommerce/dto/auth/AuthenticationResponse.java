package com.gmail.j2ee.ecommerce.dto.auth;

import com.gmail.j2ee.ecommerce.dto.user.UserResponse;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private UserResponse user;
    private String token;
}
