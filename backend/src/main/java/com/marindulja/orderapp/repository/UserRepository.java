package com.marindulja.orderapp.repository;

import com.marindulja.orderapp.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
