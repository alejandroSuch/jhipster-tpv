package such.alex.tpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import such.alex.tpv.domain.Vat;
import such.alex.tpv.service.VatService;
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
 * REST controller for managing Vat.
 */
@RestController
@RequestMapping("/api")
public class VatResource {

    private final Logger log = LoggerFactory.getLogger(VatResource.class);

    @Inject
    private VatService vatService;

    /**
     * POST  /vats -> Create a new vat.
     */
    @RequestMapping(value = "/vats",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vat> createVat(@Valid @RequestBody Vat vat) throws URISyntaxException {
        log.debug("REST request to save Vat : {}", vat);
        if (vat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vat", "idexists", "A new vat cannot already have an ID")).body(null);
        }
        Vat result = vatService.saveWithHistoric(vat);
        return ResponseEntity.created(new URI("/api/vats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vats -> Updates an existing vat.
     */
    @RequestMapping(value = "/vats",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vat> updateVat(@Valid @RequestBody Vat vat) throws URISyntaxException {
        log.debug("REST request to update Vat : {}", vat);
        if (vat.getId() == null) {
            return createVat(vat);
        }
        Vat result = vatService.saveWithHistoric(vat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vat", vat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vats -> get all the vats.
     */
    @RequestMapping(value = "/vats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Vat> getAllVats() {
        log.debug("REST request to get all Vats");
        return vatService.findAll();
            }

    /**
     * GET  /vats/:id -> get the "id" vat.
     */
    @RequestMapping(value = "/vats/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vat> getVat(@PathVariable Long id) {
        log.debug("REST request to get Vat : {}", id);
        Vat vat = vatService.findOne(id);
        return Optional.ofNullable(vat)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vats/:id -> delete the "id" vat.
     */
    @RequestMapping(value = "/vats/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVat(@PathVariable Long id) {
        log.debug("REST request to delete Vat : {}", id);
        vatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vat", id.toString())).build();
    }

    /**
     * SEARCH  /_search/vats/:query -> search for the vat corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/vats/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Vat> searchVats(@PathVariable String query) {
        log.debug("Request to search Vats for query {}", query);
        return vatService.search(query);
    }
}
