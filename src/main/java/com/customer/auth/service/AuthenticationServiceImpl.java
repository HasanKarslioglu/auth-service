package com.customer.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.customer.auth.model.Token;
import com.customer.auth.model.User;
import com.customer.auth.repository.UserRepository;

import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    //@Autowired
    //private TokenService tokenService;

    @Override
    @Transactional
    public String registerUser(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            return null; // User already exists
        }
        User user = new User(username, password);
        userRepository.save(user);
        return loginUser(username, password);
    }

    @Override
    @Transactional
    public String loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            String token = generateToken();
            user.setToken(token);
            //tokenService.saveToken(token, username);
            return token;
        }
        return null;
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    //public boolean verifyToken(String token){
        //return tokenService.getUserByToken(token) != null;
    //}
}
