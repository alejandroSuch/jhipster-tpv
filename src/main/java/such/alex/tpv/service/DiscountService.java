package such.alex.tpv.service;

import such.alex.tpv.domain.Discount;
import such.alex.tpv.repository.DiscountRepository;
import such.alex.tpv.repository.search.DiscountSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Discount.
 */
@Service
@Transactional
public class DiscountService {

    private final Logger log = LoggerFactory.getLogger(DiscountService.class);
    
    @Inject
    private DiscountRepository discountRepository;
    
    @Inject
    private DiscountSearchRepository discountSearchRepository;
    
    /**
     * Save a discount.
     * @return the persisted entity
     */
    public Discount save(Discount discount) {
        log.debug("Request to save Discount : {}", discount);
        Discount result = discountRepository.save(discount);
        discountSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the discounts.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Discount> findAll() {
        log.debug("Request to get all Discounts");
        List<Discount> result = discountRepository.findAll();
        return result;
    }

    /**
     *  get one discount by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Discount findOne(Long id) {
        log.debug("Request to get Discount : {}", id);
        Discount discount = discountRepository.findOne(id);
        return discount;
    }

    /**
     *  delete the  discount by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Discount : {}", id);
        discountRepository.delete(id);
        discountSearchRepository.delete(id);
    }

    /**
     * search for the discount corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Discount> search(String query) {
        
        log.debug("REST request to search Discounts for query {}", query);
        return StreamSupport
            .stream(discountSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
