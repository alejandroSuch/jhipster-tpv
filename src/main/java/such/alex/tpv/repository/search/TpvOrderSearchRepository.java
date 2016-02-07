package such.alex.tpv.repository.search;

import such.alex.tpv.domain.TpvOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TpvOrder entity.
 */
public interface TpvOrderSearchRepository extends ElasticsearchRepository<TpvOrder, Long> {
}
