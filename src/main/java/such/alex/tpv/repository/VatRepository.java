package such.alex.tpv.repository;

import org.springframework.stereotype.Repository;
import such.alex.tpv.domain.Vat;

import such.alex.tpv.config.custom.repository.HistoricRepository;

/**
 * Spring Data JPA repository for the Vat entity.
 */
@Repository
public interface VatRepository extends HistoricRepository<Vat,Long> {

}
