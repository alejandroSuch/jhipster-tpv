package such.alex.tpv.repository.search;

import such.alex.tpv.domain.TpvDiscountedOrderLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TpvDiscountedOrderLine entity.
 */
public interface TpvDiscountedOrderLineSearchRepository extends ElasticsearchRepository<TpvDiscountedOrderLine, Long> {
}
