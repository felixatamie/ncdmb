package com.ncdmb.canteen.controllers;

import com.ncdmb.canteen.entity.Role;
import com.ncdmb.canteen.iservice.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @GetMapping("/all")
    public ResponseEntity<List<Role>> allRoles()
    {
        return ResponseEntity.ok(rolePermissionService.getAllRoles());
    }
}
