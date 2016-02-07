package such.alex.tpv.service;

import such.alex.tpv.domain.TpvDiscountedOrderLine;
import such.alex.tpv.repository.TpvDiscountedOrderLineRepository;
import such.alex.tpv.repository.search.TpvDiscountedOrderLineSearchRepository;
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
 * Service Implementation for managing TpvDiscountedOrderLine.
 */
@Service
@Transactional
public class TpvDiscountedOrderLineService {

    private final Logger log = LoggerFactory.getLogger(TpvDiscountedOrderLineService.class);
    
    @Inject
    private TpvDiscountedOrderLineRepository tpvDiscountedOrderLineRepository;
    
    @Inject
    private TpvDiscountedOrderLineSearchRepository tpvDiscountedOrderLineSearchRepository;
    
    /**
     * Save a tpvDiscountedOrderLine.
     * @return the persisted entity
     */
    public TpvDiscountedOrderLine save(TpvDiscountedOrderLine tpvDiscountedOrderLine) {
        log.debug("Request to save TpvDiscountedOrderLine : {}", tpvDiscountedOrderLine);
        TpvDiscountedOrderLine result = tpvDiscountedOrderLineRepository.save(tpvDiscountedOrderLine);
        tpvDiscountedOrderLineSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the tpvDiscountedOrderLines.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TpvDiscountedOrderLine> findAll(Pageable pageable) {
        log.debug("Request to get all TpvDiscountedOrderLines");
        Page<TpvDiscountedOrderLine> result = tpvDiscountedOrderLineRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one tpvDiscountedOrderLine by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TpvDiscountedOrderLine findOne(Long id) {
        log.debug("Request to get TpvDiscountedOrderLine : {}", id);
        TpvDiscountedOrderLine tpvDiscountedOrderLine = tpvDiscountedOrderLineRepository.findOne(id);
        return tpvDiscountedOrderLine;
    }

    /**
     *  delete the  tpvDiscountedOrderLine by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete TpvDiscountedOrderLine : {}", id);
        tpvDiscountedOrderLineRepository.delete(id);
        tpvDiscountedOrderLineSearchRepository.delete(id);
    }

    /**
     * search for the tpvDiscountedOrderLine corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<TpvDiscountedOrderLine> search(String query) {
        
        log.debug("REST request to search TpvDiscountedOrderLines for query {}", query);
        return StreamSupport
            .stream(tpvDiscountedOrderLineSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
