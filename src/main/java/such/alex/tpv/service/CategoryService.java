package such.alex.tpv.service;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;
import reactor.fn.tuple.Tuple2;
import such.alex.tpv.domain.Category;
import such.alex.tpv.domain.Vat;
import such.alex.tpv.repository.CategoryRepository;
import such.alex.tpv.repository.search.CategorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import such.alex.tpv.service.util.Constants;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static reactor.bus.selector.Selectors.$;

/**
 * Service Implementation for managing Category.
 */
@Service
@Transactional
public class CategoryService implements Consumer<Event<Tuple2<Vat, Vat>>> {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private CategorySearchRepository categorySearchRepository;

    @Inject
    EventBus eventBus;

    /**
     * Save a category.
     * @return the persisted entity
     */
    public Category save(Category category) {
        log.debug("Request to save Category : {}", category);
        Category result = categoryRepository.save(category);
        categorySearchRepository.save(result);
        return result;
    }

    /**
     *  get all the categorys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        log.debug("Request to get all Categorys");
        List<Category> result = categoryRepository.findAll();
        return result;
    }

    /**
     *  get one category by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Category findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        Category category = categoryRepository.findOne(id);
        return category;
    }

    /**
     *  delete the  category by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.delete(id);
        categorySearchRepository.delete(id);
    }

    /**
     * search for the category corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<Category> search(String query) {

        log.debug("REST request to search Categorys for query {}", query);
        return StreamSupport
            .stream(categorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }



    @PostConstruct
    public void subscribeToEvents() {
        eventBus.on($(Constants.VAT_UPDATED), this);
    }

    @Override
    public void accept(Event<Tuple2<Vat, Vat>> event) {
        if(Constants.VAT_UPDATED.equals(event.getKey())) {
            Tuple2<Vat, Vat> data = event.getData();
            Vat oldVat = data.getT1();
            Vat newVat = data.getT2();

            List<Category> cagegories = categoryRepository.findAllByVat(oldVat);
            for (Category category: cagegories) {
                category.setVat(newVat);
            }
        }

    }
}
