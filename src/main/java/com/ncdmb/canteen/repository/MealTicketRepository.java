package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.MealTicket;
import org.springframework.data.repository.CrudRepository;

public interface MealTicketRepository extends CrudRepository<MealTicket, String> {
}
