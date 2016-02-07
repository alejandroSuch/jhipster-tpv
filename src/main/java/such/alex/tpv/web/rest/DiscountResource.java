package such.alex.tpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import such.alex.tpv.domain.Discount;
import such.alex.tpv.service.DiscountService;
import such.alex.tpv.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Discount.
 */
@RestController
@RequestMapping("/api")
public class DiscountResource {

    private final Logger log = LoggerFactory.getLogger(DiscountResource.class);
        
    @Inject
    private DiscountService discountService;
    
    /**
     * POST  /discounts -> Create a new discount.
     */
    @RequestMapping(value = "/discounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Discount> createDiscount(@Valid @RequestBody Discount discount) throws URISyntaxException {
        log.debug("REST request to save Discount : {}", discount);
        if (discount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("discount", "idexists", "A new discount cannot already have an ID")).body(null);
        }
        Discount result = discountService.save(discount);
        return ResponseEntity.created(new URI("/api/discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("discount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discounts -> Updates an existing discount.
     */
    @RequestMapping(value = "/discounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Discount> updateDiscount(@Valid @RequestBody Discount discount) throws URISyntaxException {
        log.debug("REST request to update Discount : {}", discount);
        if (discount.getId() == null) {
            return createDiscount(discount);
        }
        Discount result = discountService.save(discount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("discount", discount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discounts -> get all the discounts.
     */
    @RequestMapping(value = "/discounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Discount> getAllDiscounts() {
        log.debug("REST request to get all Discounts");
        return discountService.findAll();
            }

    /**
     * GET  /discounts/:id -> get the "id" discount.
     */
    @RequestMapping(value = "/discounts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Discount> getDiscount(@PathVariable Long id) {
        log.debug("REST request to get Discount : {}", id);
        Discount discount = discountService.findOne(id);
        return Optional.ofNullable(discount)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /discounts/:id -> delete the "id" discount.
     */
    @RequestMapping(value = "/discounts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        log.debug("REST request to delete Discount : {}", id);
        discountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("discount", id.toString())).build();
    }

    /**
     * SEARCH  /_search/discounts/:query -> search for the discount corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/discounts/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Discount> searchDiscounts(@PathVariable String query) {
        log.debug("Request to search Discounts for query {}", query);
        return discountService.search(query);
    }
}
