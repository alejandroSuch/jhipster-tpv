package such.alex.tpv.repository;

import such.alex.tpv.domain.Category;

import org.springframework.data.jpa.repository.*;
import such.alex.tpv.domain.Vat;

import java.util.List;

/**
 * Spring Data JPA repository for the Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {
    public List<Category> findAllByVat(Vat vat);
}
