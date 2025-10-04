package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.Permission;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PermissionRepository extends CrudRepository<Permission, Integer> {

    Optional<Permission>findByName(String name);
}