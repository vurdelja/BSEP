package com.bsep.bezbednosttim32.repository;

import com.bsep.bezbednosttim32.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}