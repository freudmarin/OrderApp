package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    default Role saveIfNameNotExists(Role role) {
        Optional<Role> nameExistsOptional = findByName(role.getName());
        if (nameExistsOptional.isEmpty()) {
            return save(role);
        }
        return nameExistsOptional.get();
    }
}


