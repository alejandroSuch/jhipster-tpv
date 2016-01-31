package such.alex.tpv.repository.search;

import such.alex.tpv.domain.Vat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Vat entity.
 */
public interface VatSearchRepository extends ElasticsearchRepository<Vat, Long> {
}
