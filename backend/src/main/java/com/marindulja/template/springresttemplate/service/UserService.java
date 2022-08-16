package com.marindulja.template.springresttemplate.service;


import com.marindulja.template.springresttemplate.model.User;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Gets all User stored into the system
     *
     * @return all users stored into the system
     */
    List<User> getAllUsers();

    /**
     * Find a user by its username
     *
     * @param username the username to search for
     * @return the user with the given username
     * @throws NotFoundException when the username does not exist
     */
    User findByUsername(String username) throws NotFoundException;
}
