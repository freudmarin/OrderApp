package com.marindulja.template.springresttemplate.config;

import com.marindulja.template.springresttemplate.model.Role;
import com.marindulja.template.springresttemplate.model.User;
import com.marindulja.template.springresttemplate.repository.RoleRepository;
import com.marindulja.template.springresttemplate.repository.UserRepository;
import com.marindulja.template.springresttemplate.security.ApplicationRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@Slf4j
@DependsOn("applicationRolesHandler")
public class DataGeneration {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public DataGeneration(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void generateData() {
        log.info("Starting to add dummy data!");

        List<Role> allUsers = roleRepository.findAll();
        List<Role> simpleUser = Collections.singletonList(roleRepository.findByName(ApplicationRoles.Identifier.USER).get());

        List<User> users = new ArrayList<>();
        if (userRepository.count() ==0) {
            users = Arrays.asList(
                    new User("test", passwordEncoder.encode("12345"), simpleUser),
                    new User("admin", passwordEncoder.encode("12345"), allUsers)
            );

            userRepository.saveAll(users);
        }
    }
}
