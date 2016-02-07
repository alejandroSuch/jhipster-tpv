package such.alex.tpv.service;

import such.alex.tpv.domain.TpvOrder;
import such.alex.tpv.repository.TpvOrderRepository;
import such.alex.tpv.repository.search.TpvOrderSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TpvOrder.
 */
@Service
@Transactional
public class TpvOrderService {

    private final Logger log = LoggerFactory.getLogger(TpvOrderService.class);
    
    @Inject
    private TpvOrderRepository tpvOrderRepository;
    
    @Inject
    private TpvOrderSearchRepository tpvOrderSearchRepository;
    
    /**
     * Save a tpvOrder.
     * @return the persisted entity
     */
    public TpvOrder save(TpvOrder tpvOrder) {
        log.debug("Request to save TpvOrder : {}", tpvOrder);
        TpvOrder result = tpvOrderRepository.save(tpvOrder);
        tpvOrderSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the tpvOrders.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TpvOrder> findAll(Pageable pageable) {
        log.debug("Request to get all TpvOrders");
        Page<TpvOrder> result = tpvOrderRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one tpvOrder by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TpvOrder findOne(Long id) {
        log.debug("Request to get TpvOrder : {}", id);
        TpvOrder tpvOrder = tpvOrderRepository.findOne(id);
        return tpvOrder;
    }

    /**
     *  delete the  tpvOrder by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete TpvOrder : {}", id);
        tpvOrderRepository.delete(id);
        tpvOrderSearchRepository.delete(id);
    }

    /**
     * search for the tpvOrder corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<TpvOrder> search(String query) {
        
        log.debug("REST request to search TpvOrders for query {}", query);
        return StreamSupport
            .stream(tpvOrderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
