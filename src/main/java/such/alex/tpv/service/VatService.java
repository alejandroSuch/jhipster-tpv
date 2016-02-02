package such.alex.tpv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.tuple.Tuple;
import reactor.fn.tuple.Tuple2;
import such.alex.tpv.domain.Vat;
import such.alex.tpv.repository.VatRepository;
import such.alex.tpv.repository.search.VatSearchRepository;
import such.alex.tpv.service.util.Constants;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Vat.
 */
@Service
@Transactional
public class VatService {

    private final Logger log = LoggerFactory.getLogger(VatService.class);

    @Inject
    private VatRepository vatRepository;

    @Inject
    private VatSearchRepository vatSearchRepository;

    @Inject
    EventBus eventBus;

    /**
     * Save a vat.
     * @return the persisted entity
     */
    public Vat save(Vat vat) {
        log.debug("Request to save Vat : {}", vat);
        Vat result = vatRepository.save(vat);
        vatSearchRepository.save(result);

        eventBus.notify(Constants.VAT_UPDATED, Event.wrap(Tuple.of(vat, result)));

        return result;
    }

    /**
     *  get all the vats.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Vat> findAll() {
        log.debug("Request to get all Vats");
        List<Vat> result = vatRepository.findAll();
        return result;
    }

    /**
     *  get one vat by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Vat findOne(Long id) {
        log.debug("Request to get Vat : {}", id);
        Vat vat = vatRepository.findOne(id);
        return vat;
    }

    /**
     *  delete the  vat by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vat : {}", id);
        vatRepository.delete(id);
        vatSearchRepository.delete(id);
    }

    /**
     * search for the vat corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<Vat> search(String query) {

        log.debug("REST request to search Vats for query {}", query);
        return StreamSupport
            .stream(vatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
