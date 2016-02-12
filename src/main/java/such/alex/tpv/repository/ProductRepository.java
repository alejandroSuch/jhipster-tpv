package such.alex.tpv.repository;

import such.alex.tpv.domain.Product;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select distinct product from Product product left join fetch product.discounts")
    List<Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.discounts where product.id =:id")
    Product findOneWithEagerRelationships(@Param("id") Long id);

    Product findOneByCodeAndActiveIsTrue(String Code);

}
