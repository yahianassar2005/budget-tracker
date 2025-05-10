package com.budgetapp.service;

import com.budgetapp.model.User;
import com.budgetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        if (!user.getEmail().endsWith("@gmail.com")) {
            throw new IllegalArgumentException("Email must end with @gmail.com");
        }
        return userRepository.save(user);
    }
    public Optional<User> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

}
