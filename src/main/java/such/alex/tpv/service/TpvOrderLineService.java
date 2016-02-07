package such.alex.tpv.service;

import such.alex.tpv.domain.TpvOrderLine;
import such.alex.tpv.repository.TpvOrderLineRepository;
import such.alex.tpv.repository.search.TpvOrderLineSearchRepository;
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
 * Service Implementation for managing TpvOrderLine.
 */
@Service
@Transactional
public class TpvOrderLineService {

    private final Logger log = LoggerFactory.getLogger(TpvOrderLineService.class);
    
    @Inject
    private TpvOrderLineRepository tpvOrderLineRepository;
    
    @Inject
    private TpvOrderLineSearchRepository tpvOrderLineSearchRepository;
    
    /**
     * Save a tpvOrderLine.
     * @return the persisted entity
     */
    public TpvOrderLine save(TpvOrderLine tpvOrderLine) {
        log.debug("Request to save TpvOrderLine : {}", tpvOrderLine);
        TpvOrderLine result = tpvOrderLineRepository.save(tpvOrderLine);
        tpvOrderLineSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the tpvOrderLines.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TpvOrderLine> findAll(Pageable pageable) {
        log.debug("Request to get all TpvOrderLines");
        Page<TpvOrderLine> result = tpvOrderLineRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one tpvOrderLine by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TpvOrderLine findOne(Long id) {
        log.debug("Request to get TpvOrderLine : {}", id);
        TpvOrderLine tpvOrderLine = tpvOrderLineRepository.findOne(id);
        return tpvOrderLine;
    }

    /**
     *  delete the  tpvOrderLine by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete TpvOrderLine : {}", id);
        tpvOrderLineRepository.delete(id);
        tpvOrderLineSearchRepository.delete(id);
    }

    /**
     * search for the tpvOrderLine corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<TpvOrderLine> search(String query) {
        
        log.debug("REST request to search TpvOrderLines for query {}", query);
        return StreamSupport
            .stream(tpvOrderLineSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
