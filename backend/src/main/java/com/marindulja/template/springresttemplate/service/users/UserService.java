package com.marindulja.template.springresttemplate.service.users;

import com.marindulja.template.springresttemplate.dto.UserDto;
import com.marindulja.template.springresttemplate.model.User;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Gets all User stored into the system
     *
     * @return all users stored into the system
     */
    Page<UserDto> getPaginatedAndFilteredUsers(Pageable pageRequest, String searchValue);

    List<UserDto> getAllUsers();

    List<String> getAllRoles();

    /**
     * Find a user by its username
     *
     * @param username the username to search for
     * @return the user with the given username
     * @throws NotFoundException when the username does not exist
     */
    User findByUsername(String username) throws NotFoundException;

    UserDto addUser(UserDto userToBeAdded);

    ResponseEntity<UserDto> getUserById(long id);

    ResponseEntity<UserDto> updateUserById(long id, UserDto user);

    ResponseEntity<HttpStatus> deleteUserById(long id);
}
