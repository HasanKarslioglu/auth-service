package com.customer.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.customer.auth.model.Token;
import com.customer.auth.model.User;
import com.customer.auth.repository.UserRepository;

import java.security.MessageDigest;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public Token registerUser(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            return null; // User already exists
        }
        String hashedPassword = hashWithSHA256(password);
        User user = new User(username, hashedPassword);
        userRepository.save(user);
        return loginUser(username, password);
    }

    @Override
    @Transactional
    public Token loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        String hashedPassword = hashWithSHA256(password);
        if (user != null && user.getPassword().equals(hashedPassword)) {
            String token = generateToken();
            String refToken = generateToken();
            tokenService.saveToken(token, username);
            tokenService.saveRefreshToken(refToken, username);
            return new Token(token,refToken,username);
        }
        return null;
    }

    @Override
    @Transactional
    public Token refreshToken(Token oldToken){
        boolean isTokenValid = tokenService.isRefreshTokenValid(oldToken.getRefToken());
        boolean isUserValid = tokenService.getUsernameByToken(oldToken.getRefToken()).equals(oldToken.getUsername());
        if (isTokenValid && isUserValid){
            Token newToken = new Token(generateToken(), generateToken(), oldToken.getUsername());
            tokenService.saveToken(newToken.getToken(), newToken.getUsername());
            tokenService.saveRefreshToken(newToken.getRefToken(), newToken.getUsername());
            tokenService.invalidateToken(oldToken.getRefToken());
            tokenService.invalidateToken(oldToken.getToken());
            return newToken;
        }
        return null;
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean verifyToken(String token){
        return tokenService.getUsernameByToken(token) != null;
    }

    public static String hashWithSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
