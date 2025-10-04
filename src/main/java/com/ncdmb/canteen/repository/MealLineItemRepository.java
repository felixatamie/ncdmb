
package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.MealLineItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealLineItemRepository extends CrudRepository<MealLineItem, Integer> {

    @Query(value = "SELECT * FROM meal_line_item WHERE transaction_id = :transactionId", nativeQuery = true)
    List<MealLineItem> findByTransactionId(@Param("transactionId") Integer transactionId);

}