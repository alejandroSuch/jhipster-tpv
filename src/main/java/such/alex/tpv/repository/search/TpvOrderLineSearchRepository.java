package such.alex.tpv.repository.search;

import such.alex.tpv.domain.TpvOrderLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TpvOrderLine entity.
 */
public interface TpvOrderLineSearchRepository extends ElasticsearchRepository<TpvOrderLine, Long> {
}
