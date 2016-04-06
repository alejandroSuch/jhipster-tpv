package such.alex.tpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import such.alex.tpv.domain.TpvOrderLine;
import such.alex.tpv.service.TpvOrderLineService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TpvOrderLine.
 */
@RestController
@RequestMapping("/api")
public class TpvOrderLineResource {

    private final Logger log = LoggerFactory.getLogger(TpvOrderLineResource.class);

    @Inject
    private TpvOrderLineService tpvOrderLineService;

    /**
     * POST  /tpvOrderLines -> Create a new tpvOrderLine.
     */
    @RequestMapping(value = "/tpvOrderLines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvOrderLine> createTpvOrderLine(@Valid @RequestBody TpvOrderLine tpvOrderLine) throws URISyntaxException {
        log.debug("REST request to save TpvOrderLine : {}", tpvOrderLine);
        if (tpvOrderLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tpvOrderLine", "idexists", "A new tpvOrderLine cannot already have an ID")).body(null);
        }
        TpvOrderLine result = tpvOrderLineService.save(tpvOrderLine);
        return ResponseEntity.created(new URI("/api/tpvOrderLines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tpvOrderLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tpvOrderLines -> Updates an existing tpvOrderLine.
     */
    @RequestMapping(value = "/tpvOrderLines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvOrderLine> updateTpvOrderLine(@Valid @RequestBody TpvOrderLine tpvOrderLine) throws URISyntaxException {
        log.debug("REST request to update TpvOrderLine : {}", tpvOrderLine);
        if (tpvOrderLine.getId() == null) {
            return createTpvOrderLine(tpvOrderLine);
        }
        TpvOrderLine result = tpvOrderLineService.save(tpvOrderLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tpvOrderLine", tpvOrderLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tpvOrderLines -> get all the tpvOrderLines.
     */
    @RequestMapping(value = "/tpvOrderLines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TpvOrderLine>> getAllTpvOrderLines(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TpvOrderLines");
        Page<TpvOrderLine> page = tpvOrderLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tpvOrderLines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tpvOrderLines/:id -> get the "id" tpvOrderLine.
     */
    @RequestMapping(value = "/tpvOrderLines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TpvOrderLine> getTpvOrderLine(@PathVariable Long id) {
        log.debug("REST request to get TpvOrderLine : {}", id);
        TpvOrderLine tpvOrderLine = tpvOrderLineService.findOne(id);
        return Optional.ofNullable(tpvOrderLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tpvOrderLines/:id -> delete the "id" tpvOrderLine.
     */
    @RequestMapping(value = "/tpvOrderLines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTpvOrderLine(@PathVariable Long id) {
        log.debug("REST request to delete TpvOrderLine : {}", id);
        tpvOrderLineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tpvOrderLine", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tpvOrderLines/:query -> search for the tpvOrderLine corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/tpvOrderLines/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TpvOrderLine> searchTpvOrderLines(@PathVariable String query) {
        log.debug("Request to search TpvOrderLines for query {}", query);
        return tpvOrderLineService.search(query);
    }
}
