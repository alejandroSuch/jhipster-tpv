package such.alex.tpv.repository;

import such.alex.tpv.domain.Price;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Price entity.
 */
public interface PriceRepository extends JpaRepository<Price,Long> {

}
