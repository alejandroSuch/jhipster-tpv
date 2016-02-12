package such.alex.tpv.repository;

import such.alex.tpv.domain.Discount;

import org.springframework.data.jpa.repository.*;
import such.alex.tpv.domain.Product;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Discount entity.
 */
public interface DiscountRepository extends JpaRepository<Discount,Long> {

    @Query(
        "SELECT d " +
        "FROM Product p " +
            "JOIN p.discounts d " +
        "WHERE p = ?1 " +
            "AND d.activeFrom <= ?2 " +
            "AND d.activeTo >= ?2"
    )
    public Discount findOneActiveForProductAndDate(Product p, LocalDate date);

}
