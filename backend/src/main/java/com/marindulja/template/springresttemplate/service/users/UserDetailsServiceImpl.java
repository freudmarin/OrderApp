package com.marindulja.template.springresttemplate.service.users;

import com.marindulja.template.springresttemplate.adapters.UserAdapter;
import com.marindulja.template.springresttemplate.dto.UserDto;
import com.marindulja.template.springresttemplate.model.Role;
import com.marindulja.template.springresttemplate.model.User;
import com.marindulja.template.springresttemplate.repository.UserRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public PageImpl<UserDto> getPaginatedAndFilteredUsers(Pageable pageRequest, String searchValue) {
        Page<User> pageResult = userRepository.findAll(pageRequest);
        List<UserDto> usersDto = pageResult
                .stream()
                .filter(res -> res.getFullName().contains(searchValue) || res.getUsername().contains(searchValue)
                        || res.getJobTitle().contains(searchValue))
                .map(this::mapToDTO)
                .collect(toList());
        return new PageImpl<>(usersDto, pageRequest, usersDto.size());
    }

    @Override
    public List<UserDto> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false).map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<String> getAllRoles() {
        return Arrays.stream(Role.values()).map(Role::getAuthority).collect(Collectors.toList());

    }

    @Override
    public User findByUsername(String username) throws NotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundException(String.format("User with the username %s does not exist!", username))
                );
    }

    @Override
    public UserDto addUser(UserDto userToBeAdded) {
        User user = new User();
        user.setUsername(userToBeAdded.getUsername());
        user.setPassword(passwordEncoder.encode(userToBeAdded.getPassword()));
        user.setJobTitle(userToBeAdded.getJobTitle());
        user.setFullName(userToBeAdded.getFullName());
        user.setRole(userToBeAdded.getRole());
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    public ResponseEntity<UserDto> getUserById(long id) {
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            return new ResponseEntity<>(mapToDTO(userData.get()), HttpStatus.OK);
        } else {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("User not found");
        }
    }

    public ResponseEntity<UserDto> updateUserById(long id, UserDto userDto) {
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setRole(userDto.getRole());
            _user.setUsername(userDto.getUsername());
            _user.setFullName(userDto.getFullName());
            _user.setJobTitle(userDto.getJobTitle());
            _user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User updatedUser = userRepository.save(_user);
            return new ResponseEntity<>(mapToDTO(updatedUser), HttpStatus.OK);
        } else {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("User not found");
        }
    }

    public ResponseEntity<HttpStatus> deleteUserById(long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("User not found");
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user for authentication spring security");
        try {
            User user = findByUsername(username);
            String role = user.getRole().getAuthority();
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_" + role);
            return new UserAdapter(user.getId(), user.getPassword(), grantedAuthorities, userRepository);

        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }

    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role_user) {
        return Collections.singletonList(new SimpleGrantedAuthority(role_user));
    }


    private UserDto mapToDTO(User user) {
        return mapper.map(user, UserDto.class);
    }

    private User mapToEntity(UserDto userDto) {
        return mapper.map(userDto, User.class);
    }
}
