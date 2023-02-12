package com.marindulja.template.springresttemplate.config;

import com.marindulja.template.springresttemplate.model.Role;
import com.marindulja.template.springresttemplate.model.User;
import com.marindulja.template.springresttemplate.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
public class DataGeneration {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public DataGeneration(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void generateData() {
        log.info("Starting to add dummy data!");

        List<User> users = new ArrayList<>();
        if (userRepository.count() ==0) {
            users = Arrays.asList(
                    new User("test", passwordEncoder.encode("12345"), "Test", "Seller", Role.USER),
                    new User("admin", passwordEncoder.encode("12345"),"Marin Dulja", "Shop Manager",
                            Role.ADMIN));
            userRepository.saveAll(users);
        }
    }
}
