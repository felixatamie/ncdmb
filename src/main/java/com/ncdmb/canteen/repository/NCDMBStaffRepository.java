package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.NCDMBStaff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NCDMBStaffRepository extends CrudRepository<NCDMBStaff, Integer> {

    Optional<NCDMBStaff> findByStaffId(String staffId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE NCDMBStaff s SET s.status = :status WHERE s.id = :id")
    int updateStatusById(@Param("id") Integer id, @Param("status") NCDMBStaff.Status status);

}

