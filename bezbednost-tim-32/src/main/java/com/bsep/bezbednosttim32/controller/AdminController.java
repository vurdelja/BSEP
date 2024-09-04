package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.model.Permission;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @PostMapping("/roles")
    public Role createRole(@RequestParam String roleName, @RequestBody Set<Permission> permissions) {
        return adminService.createRole(roleName, permissions);
    }

    @PreAuthorize("hasAuthority('MANAGE_PERMISSIONS')")
    @PostMapping("/permissions")
    public Permission createPermission(@RequestParam String permissionName) {
        return adminService.createPermission(permissionName);
    }
}
