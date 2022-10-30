package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.UserDto;
import com.marindulja.template.springresttemplate.model.Role;
import com.marindulja.template.springresttemplate.service.AuthService;
import com.marindulja.template.springresttemplate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UsersController {

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private AuthService authService;

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody UserDto userDto) {
      return new ResponseEntity(userDetailsService.addUser(userDto), HttpStatus.OK);
    }

    @GetMapping("/")
    public List<UserDto> getAllUsers() {
        return userDetailsService.getAllUsers();
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
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
