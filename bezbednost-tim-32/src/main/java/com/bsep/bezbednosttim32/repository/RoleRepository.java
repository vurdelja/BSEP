package com.bsep.bezbednosttim32.repository;

import com.bsep.bezbednosttim32.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
