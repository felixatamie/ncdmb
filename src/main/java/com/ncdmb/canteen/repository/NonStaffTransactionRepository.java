package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.NonStaffTransaction;
import org.springframework.data.repository.CrudRepository;

public interface NonStaffTransactionRepository extends CrudRepository<NonStaffTransaction, Integer> {
}
