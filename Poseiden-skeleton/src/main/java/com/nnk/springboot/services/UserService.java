package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UsernameAlreadyExistingException;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * createUser method to create a new user with given details
     *
     * @param user provided by create user form
     * @throws UsernameAlreadyExistingException if username given in user object is already taken
     * @author Clara SLYS
     */
    @Transactional
    public void createUser(User user) throws UsernameAlreadyExistingException {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (!ObjectUtils.isEmpty(existingUser)) {
            throw new UsernameAlreadyExistingException("An account with this username already exists. Please use another username.");
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }
}
