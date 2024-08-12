package com.customer.auth.service;
import com.customer.auth.model.Token;

public interface AuthenticationService {
    Token registerUser(String username, String password);
    Token loginUser(String username, String password);
    boolean verifyToken(String token);
    Token refreshToken(Token oldToken);
}
