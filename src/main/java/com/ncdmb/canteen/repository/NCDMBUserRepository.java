package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.CanteenUser;
import com.ncdmb.canteen.entity.NCDMBUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NCDMBUserRepository extends CrudRepository<NCDMBUser, Integer> {

    Optional<NCDMBUser> findByUsername(String username);
}
