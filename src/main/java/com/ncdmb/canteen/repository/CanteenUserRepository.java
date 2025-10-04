package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.CanteenUser;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CanteenUserRepository extends CrudRepository<CanteenUser, Integer> {
    void deleteByIdAndCanteen_Id(int id, int canteenId);
    List<CanteenUser> findByCanteen_Id(int canteenId);
    Optional<CanteenUser> findByUsername(String username);
    Optional<CanteenUser> findByIdAndCanteen_Id(int id, int canteenId);
    @Modifying
    @Query("UPDATE CanteenUser cu SET cu.isActive = :isActive WHERE cu.id = :id AND cu.canteen.id = :canteenId")
    int updateIsActiveByIdAndCanteenId(@Param("id") Integer id,
                                       @Param("canteenId") Integer canteenId,
                                       @Param("isActive") Boolean isActive);
}

