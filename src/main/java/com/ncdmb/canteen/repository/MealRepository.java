package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.Meal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface MealRepository extends CrudRepository<Meal, Integer> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
			UPDATE meal
			SET
			name  = CASE WHEN ?1 IS NOT NULL THEN ?1 ELSE name END,
			price = CASE WHEN ?2 IS NOT NULL THEN ?2 ELSE price END,
			is_available = CASE WHEN ?3 IS NOT NULL THEN ?3 ELSE is_available END
			WHERE id = ?4
			""", nativeQuery = true)
    void updateMeal(String name, BigDecimal price, boolean isAvailable, int id);

	Optional<Meal> findByName(String name);

	Optional<Meal> findByIdAndCanteen_Id(int id, int canteenId);

	List<Meal> findByCanteen_Id(int canteenId, Sort sort);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
			delete from meal
			WHERE id = ?1 AND canteen_id = ?2
			""", nativeQuery = true)
    void removeByIdAndCanteen_Id(int id, int canteenId);


}

