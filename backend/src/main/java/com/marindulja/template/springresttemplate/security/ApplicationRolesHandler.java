package com.marindulja.template.springresttemplate.security;

import com.marindulja.template.springresttemplate.model.Role;
import com.marindulja.template.springresttemplate.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class ApplicationRolesHandler {

    private final RoleRepository roleRepository;

    public ApplicationRolesHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void saveRolesToDataBase() {
        Stream.of(ApplicationRoles.values())
                .map(ApplicationRoles::getName)
                .map(Role::new)
                .map(roleRepository::saveIfNameNotExists)
                .forEach(x -> log.info("Created new role: {}", x.getName()));
    }

}
