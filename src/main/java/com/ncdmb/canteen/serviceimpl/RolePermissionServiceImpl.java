package com.ncdmb.canteen.serviceimpl;

import com.ncdmb.canteen.entity.Permission;
import com.ncdmb.canteen.entity.Role;
import com.ncdmb.canteen.iservice.RolePermissionService;
import com.ncdmb.canteen.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class RolePermissionServiceImpl implements RolePermissionService {

    private final RoleRepository roleRepository;
    @Override
    public List<Role> getAllRoles() {

        Iterable<Role> all = roleRepository.findAll();
        return (List<Role>) all;
    }
}
