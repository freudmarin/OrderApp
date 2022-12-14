package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.UserDto;
import com.marindulja.template.springresttemplate.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UsersController {

    private final UserService userDetailsService;

    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
      return new ResponseEntity<>(userDetailsService.addUser(userDto), HttpStatus.OK);
    }

    @GetMapping("/")
    public List<UserDto> getAllUsers() {
        return userDetailsService.getAllUsers();
    }

    @GetMapping("/roles")
    public List<String> getAllRoles() {
        return userDetailsService.getAllRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id) {
       return userDetailsService.getUserById(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") long id, @RequestBody UserDto user) {
        return userDetailsService.updateUserById(id, user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        return userDetailsService.deleteUserById(id);
    }
}
