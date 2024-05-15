package com.bsep.bezbednosttim32.repository;

import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

}
