package com.customer.auth.service;

import com.customer.auth.model.Token;
import com.customer.auth.model.User;

public interface AuthenticationService {
    String registerUser(String username, String password);
    String loginUser(String username, String password);
}
