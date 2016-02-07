package such.alex.tpv.repository;

import such.alex.tpv.domain.TpvOrderLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TpvOrderLine entity.
 */
public interface TpvOrderLineRepository extends JpaRepository<TpvOrderLine,Long> {

}
