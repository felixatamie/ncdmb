package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.Canteen;
import org.springframework.data.repository.CrudRepository;

public interface CanteenRepository extends CrudRepository<Canteen, Integer> {
}

