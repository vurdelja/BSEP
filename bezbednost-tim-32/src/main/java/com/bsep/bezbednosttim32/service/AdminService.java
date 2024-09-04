package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.model.Permission;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.repository.PermissionRepository;
import com.bsep.bezbednosttim32.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public Role createRole(String roleName, Set<Permission> permissions) {
        Role role = Role.builder()
                .name(roleName)
                .permissions(permissions)
                .build();
        return roleRepository.save(role);
    }

    public Role updateRolePermissions(Integer roleId, Set<Permission> permissions) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    public Permission createPermission(String name) {
        Permission permission = Permission.builder()
                .name(name)
                .build();
        return permissionRepository.save(permission);
    }
}
