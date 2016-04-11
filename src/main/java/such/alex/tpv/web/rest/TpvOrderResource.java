package such.alex.tpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import such.alex.tpv.domain.TpvOrder;
import such.alex.tpv.domain.TpvOrderLine;
import such.alex.tpv.service.ProductService;
import such.alex.tpv.service.TpvOrderService;
import such.alex.tpv.web.rest.util.HeaderUtil;
import such.alex.tpv.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

/**
 * REST controller for managing TpvOrder.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TpvOrderResource {

    private final Logger log = LoggerFactory.getLogger(TpvOrderResource.class);

    @Inject
    private TpvOrderService tpvOrderService;

    @Inject
    private ProductService productService;

    /**
     * POST  /tpvOrders -> Create a new tpvOrder.
     */
    @RequestMapping(value = "/tpvOrders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvOrder> createTpvOrder(@Valid @RequestBody TpvOrder tpvOrder) throws URISyntaxException {
        log.debug("REST request to save TpvOrder : {}", tpvOrder);
        if (tpvOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tpvOrder", "idexists", "A new tpvOrder cannot already have an ID")).body(null);
        }
        TpvOrder result = tpvOrderService.save(tpvOrder);
        return ResponseEntity.created(new URI("/api/tpvOrders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tpvOrder", result.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/tpvOrders/new",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvOrder> createNew() throws URISyntaxException {
        TpvOrder result = tpvOrderService.save(new TpvOrder());
        return ResponseEntity.created(new URI("/api/tpvOrders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tpvOrder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tpvOrders -> Updates an existing tpvOrder.
     */
    @RequestMapping(value = "/tpvOrders",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvOrder> updateTpvOrder(@Valid @RequestBody TpvOrder tpvOrder) throws URISyntaxException {
        log.debug("REST request to update TpvOrder : {}", tpvOrder);
        if (tpvOrder.getId() == null) {
            return createTpvOrder(tpvOrder);
        }
        TpvOrder result = tpvOrderService.save(tpvOrder);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("tpvOrder", tpvOrder.getId().toString()))
                .body(result);
    }

    /**
     * GET  /tpvOrders -> get all the tpvOrders.
     */
    @RequestMapping(value = "/tpvOrders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TpvOrder>> getAllTpvOrders(Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of TpvOrders");
        Page<TpvOrder> page = tpvOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tpvOrders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tpvOrders/:id -> get the "id" tpvOrder.
     */
    @RequestMapping(value = "/tpvOrders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public TpvOrder getTpvOrder(@PathVariable Long id) {
        log.debug("REST request to get TpvOrder : {}", id);
        TpvOrder tpvOrder = tpvOrderService.findOne(id);

        if(tpvOrder.getLines().isEmpty()) {
            log.info("Order has no lines");
        }

        return tpvOrder;

        /*return Optional.ofNullable(tpvOrder)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));*/
    }

    /**
     * GET  /tpvOrders/:id -> get the "id" tpvOrder.
     */
    @RequestMapping(value = "/tpvOrders/{id}/lines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Collection<TpvOrderLine> getTpvOrderLines(@PathVariable Long id) {
        log.debug("REST request to get TpvOrder : {}", id);
        TpvOrder tpvOrder = tpvOrderService.findOne(id);

        if(tpvOrder.getLines().isEmpty()) {
            log.info("Order has no lines");
        }

        return tpvOrder.getLines();

        /*return Optional.ofNullable(tpvOrder)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));*/
    }

    /**
     * DELETE  /tpvOrders/:id -> delete the "id" tpvOrder.
     */
    @RequestMapping(value = "/tpvOrders/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTpvOrder(@PathVariable Long id) {
        log.debug("REST request to delete TpvOrder : {}", id);
        tpvOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tpvOrder", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tpvOrders/:query -> search for the tpvOrder corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/tpvOrders/{query:.+}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TpvOrder> searchTpvOrders(@PathVariable String query) {
        log.debug("Request to search TpvOrders for query {}", query);
        return tpvOrderService.search(query);
    }

    @RequestMapping(value = "/tpvOrders/{orderId}/product/{productId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvOrder> addProduct(@PathVariable Long orderId, @PathVariable Long productId) {
        final TpvOrder tpvOrder = tpvOrderService.addProduct(orderId, productId);

        return new ResponseEntity<>(tpvOrder, HttpStatus.OK);
    }

    @RequestMapping(value = "/tpvOrders/{orderId}/product/{productId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvOrder> removeProduct(@PathVariable Long orderId, @PathVariable Long productId) {
        final TpvOrder tpvOrder = tpvOrderService.removeProduct(orderId, productId);

        return new ResponseEntity<>(tpvOrder, HttpStatus.OK);
    }

    @RequestMapping(value = "/tpvOrders/{orderId}/next-state",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvOrder> handleNextState(@PathVariable Long orderId) {
        final TpvOrder tpvOrder = tpvOrderService.handleNextState(orderId);

        return new ResponseEntity<>(tpvOrder, HttpStatus.OK);
    }
}
