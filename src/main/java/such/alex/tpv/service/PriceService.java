package such.alex.tpv.service;

import such.alex.tpv.domain.Price;
import such.alex.tpv.repository.PriceRepository;
import such.alex.tpv.repository.search.PriceSearchRepository;
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
 * Service Implementation for managing Price.
 */
@Service
@Transactional
public class PriceService {

    private final Logger log = LoggerFactory.getLogger(PriceService.class);
    
    @Inject
    private PriceRepository priceRepository;
    
    @Inject
    private PriceSearchRepository priceSearchRepository;
    
    /**
     * Save a price.
     * @return the persisted entity
     */
    public Price save(Price price) {
        log.debug("Request to save Price : {}", price);
        Price result = priceRepository.save(price);
        priceSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the prices.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Price> findAll(Pageable pageable) {
        log.debug("Request to get all Prices");
        Page<Price> result = priceRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one price by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Price findOne(Long id) {
        log.debug("Request to get Price : {}", id);
        Price price = priceRepository.findOne(id);
        return price;
    }

    /**
     *  delete the  price by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Price : {}", id);
        priceRepository.delete(id);
        priceSearchRepository.delete(id);
    }

    /**
     * search for the price corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Price> search(String query) {
        
        log.debug("REST request to search Prices for query {}", query);
        return StreamSupport
            .stream(priceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
