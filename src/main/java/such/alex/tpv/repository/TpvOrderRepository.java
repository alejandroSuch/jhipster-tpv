package such.alex.tpv.repository;

import such.alex.tpv.domain.TpvOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TpvOrder entity.
 */
public interface TpvOrderRepository extends JpaRepository<TpvOrder,Long> {

}
