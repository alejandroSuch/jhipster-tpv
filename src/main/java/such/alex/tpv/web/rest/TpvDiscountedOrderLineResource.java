package such.alex.tpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import such.alex.tpv.domain.TpvDiscountedOrderLine;
import such.alex.tpv.service.TpvDiscountedOrderLineService;
import such.alex.tpv.web.rest.util.HeaderUtil;
import such.alex.tpv.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TpvDiscountedOrderLine.
 */
@RestController
@RequestMapping("/api")
public class TpvDiscountedOrderLineResource {

    private final Logger log = LoggerFactory.getLogger(TpvDiscountedOrderLineResource.class);
        
    @Inject
    private TpvDiscountedOrderLineService tpvDiscountedOrderLineService;
    
    /**
     * POST  /tpvDiscountedOrderLines -> Create a new tpvDiscountedOrderLine.
     */
    @RequestMapping(value = "/tpvDiscountedOrderLines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvDiscountedOrderLine> createTpvDiscountedOrderLine(@RequestBody TpvDiscountedOrderLine tpvDiscountedOrderLine) throws URISyntaxException {
        log.debug("REST request to save TpvDiscountedOrderLine : {}", tpvDiscountedOrderLine);
        if (tpvDiscountedOrderLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tpvDiscountedOrderLine", "idexists", "A new tpvDiscountedOrderLine cannot already have an ID")).body(null);
        }
        TpvDiscountedOrderLine result = tpvDiscountedOrderLineService.save(tpvDiscountedOrderLine);
        return ResponseEntity.created(new URI("/api/tpvDiscountedOrderLines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tpvDiscountedOrderLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tpvDiscountedOrderLines -> Updates an existing tpvDiscountedOrderLine.
     */
    @RequestMapping(value = "/tpvDiscountedOrderLines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvDiscountedOrderLine> updateTpvDiscountedOrderLine(@RequestBody TpvDiscountedOrderLine tpvDiscountedOrderLine) throws URISyntaxException {
        log.debug("REST request to update TpvDiscountedOrderLine : {}", tpvDiscountedOrderLine);
        if (tpvDiscountedOrderLine.getId() == null) {
            return createTpvDiscountedOrderLine(tpvDiscountedOrderLine);
        }
        TpvDiscountedOrderLine result = tpvDiscountedOrderLineService.save(tpvDiscountedOrderLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tpvDiscountedOrderLine", tpvDiscountedOrderLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tpvDiscountedOrderLines -> get all the tpvDiscountedOrderLines.
     */
    @RequestMapping(value = "/tpvDiscountedOrderLines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TpvDiscountedOrderLine>> getAllTpvDiscountedOrderLines(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TpvDiscountedOrderLines");
        Page<TpvDiscountedOrderLine> page = tpvDiscountedOrderLineService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tpvDiscountedOrderLines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tpvDiscountedOrderLines/:id -> get the "id" tpvDiscountedOrderLine.
     */
    @RequestMapping(value = "/tpvDiscountedOrderLines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvDiscountedOrderLine> getTpvDiscountedOrderLine(@PathVariable Long id) {
        log.debug("REST request to get TpvDiscountedOrderLine : {}", id);
        TpvDiscountedOrderLine tpvDiscountedOrderLine = tpvDiscountedOrderLineService.findOne(id);
        return Optional.ofNullable(tpvDiscountedOrderLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tpvDiscountedOrderLines/:id -> delete the "id" tpvDiscountedOrderLine.
     */
    @RequestMapping(value = "/tpvDiscountedOrderLines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTpvDiscountedOrderLine(@PathVariable Long id) {
        log.debug("REST request to delete TpvDiscountedOrderLine : {}", id);
        tpvDiscountedOrderLineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tpvDiscountedOrderLine", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tpvDiscountedOrderLines/:query -> search for the tpvDiscountedOrderLine corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/tpvDiscountedOrderLines/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TpvDiscountedOrderLine> searchTpvDiscountedOrderLines(@PathVariable String query) {
        log.debug("Request to search TpvDiscountedOrderLines for query {}", query);
        return tpvDiscountedOrderLineService.search(query);
    }
}
