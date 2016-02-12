package such.alex.tpv.service;

import org.springframework.util.CollectionUtils;
import such.alex.tpv.domain.*;
import such.alex.tpv.repository.TpvOrderLineRepository;
import such.alex.tpv.repository.TpvOrderRepository;
import such.alex.tpv.repository.search.TpvOrderSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import such.alex.tpv.state.order.OrderState;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TpvOrder.
 */
@Service
@Transactional
public class TpvOrderService {

    private final Logger log = LoggerFactory.getLogger(TpvOrderService.class);

    @Inject
    private TpvOrderRepository tpvOrderRepository;

    @Inject
    private DiscountService discountService;

    @Inject
    private TpvOrderSearchRepository tpvOrderSearchRepository;

    @Inject
    private TpvOrderLineRepository tpvOrderLineRepository;

    /**
     * Save a tpvOrder.
     *
     * @return the persisted entity
     */
    public TpvOrder save(TpvOrder tpvOrder) {
        log.debug("Request to save TpvOrder : {}", tpvOrder);
        TpvOrder result = tpvOrderRepository.save(tpvOrder);
        tpvOrderSearchRepository.save(result);
        return result;
    }

    /**
     * get all the tpvOrders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TpvOrder> findAll(Pageable pageable) {
        log.debug("Request to get all TpvOrders");
        Page<TpvOrder> result = tpvOrderRepository.findAll(pageable);
        return result;
    }

    /**
     * get one tpvOrder by id.
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TpvOrder findOne(Long id) {
        log.debug("Request to get TpvOrder : {}", id);
        TpvOrder tpvOrder = tpvOrderRepository.findOne(id);
        return tpvOrder;
    }

    /**
     * delete the  tpvOrder by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete TpvOrder : {}", id);
        tpvOrderRepository.delete(id);
        tpvOrderSearchRepository.delete(id);
    }

    /**
     * search for the tpvOrder corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<TpvOrder> search(String query) {

        log.debug("REST request to search TpvOrders for query {}", query);
        return StreamSupport
            .stream(tpvOrderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public TpvOrder createNew() {
        return new TpvOrder();
    }

    public void handleNextState(TpvOrder order) {
        OrderState state = order.getState();
        state.handleNext(order);
    }

    public void handlePrevState(TpvOrder order) {
        OrderState state = order.getState();
        state.handlePrev(order);
    }

    public TpvOrder addProduct(TpvOrder order, Product product) {
        if (OrderState.COMPLETE.equals(order.getState())) {
            throw new TpvOrderException("Cannot perform this action on complete orders");
        }

        if (!addToExistingLines(order, product)) {
            addNewLine(order, product);
        }

        if (OrderState.EMPTY.equals(order.getState())) {
            this.handleNextState(order);
        }

        return this.save(order);
    }

    public TpvOrder removeProduct(TpvOrder order, Product product) {
        if (OrderState.COMPLETE.equals(order.getState())) {
            throw new TpvOrderException("Cannot perform this action on complete orders");
        }

        final Collection<TpvOrderLine> lines = order.getLines();

        int removedLineNumber = decrementLine(product, lines);
        if (removedLineNumber != -1 && !CollectionUtils.isEmpty(lines)) {
            updateLineNumbers(lines, removedLineNumber);
        }

        if (CollectionUtils.isEmpty(order.getLines())) {
            this.handlePrevState(order);
        }

        return this.save(order);
    }

    private int decrementLine(Product product, Collection<TpvOrderLine> lines) {
        int removedLineNumber = -1;

        for (TpvOrderLine line : lines) {
            if (line.getProduct().equals(product)) {
                line.decrement();
                if (line.getQty() <= 0) {
                    removedLineNumber = line.getLineNumber();
                    lines.remove(line);
                    tpvOrderLineRepository.delete(line);
                    break;
                }
            }
        }
        return removedLineNumber;
    }

    private void updateLineNumbers(Collection<TpvOrderLine> lines, int removedLineNumber) {
        for (TpvOrderLine line : lines) {
            final Integer lineNumber = line.getLineNumber();
            if (lineNumber > removedLineNumber) {
                line.setLineNumber(lineNumber - 1);
            }
        }
    }


    private void addNewLine(TpvOrder order, Product product) {
        final Discount discount = discountService.getActiveDiscountForProduct(product);

        final TpvOrderLine line = discount == null ? new TpvOrderLine() : new TpvDiscountedOrderLine(discount);

        line
            .setLineNumber(order.getLines().size() + 1)
            .setProduct(product)
            .setPrice(product.getPrice())
            .setQty(1)
            .setVat(
                product
                    .getCategory()
                    .getVat()
            );

        order.getLines().add(line);
    }

    private boolean addToExistingLines(TpvOrder order, Product product) {
        final Collection<TpvOrderLine> lines = order.getLines();

        for (TpvOrderLine line : lines) {
            if (line.getProduct().equals(product)) {
                line.increment();
                return true;
            }
        }
        return false;
    }
}
