package such.alex.tpv.repository;

import such.alex.tpv.domain.TpvDiscountedOrderLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TpvDiscountedOrderLine entity.
 */
public interface TpvDiscountedOrderLineRepository extends JpaRepository<TpvDiscountedOrderLine,Long> {

}
