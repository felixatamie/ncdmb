package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.entity.Cadre;
import com.ncdmb.canteen.entity.Permission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CadreRepository extends CrudRepository<Cadre, Integer> {


	Optional<Cadre> findByName(String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
			UPDATE cadre
			SET
			name  = CASE WHEN ?1 IS NOT NULL THEN ?1 ELSE name END,
			ncdmb_percent = CASE WHEN ?2 IS NOT NULL THEN ?2 ELSE ncdmb_percent END,
			staff_percent = CASE WHEN ?3 IS NOT NULL THEN ?3 ELSE staff_percent END,
			modified_at = CASE WHEN ?4 IS NOT NULL THEN ?4 ELSE modified_at END
			WHERE id = ?5
			""", nativeQuery = true)
    void updateCadre(String name, BigDecimal ncdmbPercent, BigDecimal staffPercent, LocalDateTime modifyAt, int id);
}

