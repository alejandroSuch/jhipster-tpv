package such.alex.tpv.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import such.alex.tpv.Application;
import such.alex.tpv.domain.TpvOrder;
import such.alex.tpv.domain.TpvOrderLine;
import such.alex.tpv.state.order.OrderState;
import such.alex.tpv.state.order.OrderStateException;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class TpvOrderServiceTest {
    @Inject
    private TpvOrderService orderService;

    @Test(expected = OrderStateException.class)
    public void expectExceptionOnEmptyOrderNext() {
        TpvOrder newOrder = createNewOrder();
        TpvOrder order = orderService.findOne(newOrder.getId());

        orderService.handleNextState(order);
    }

    @Test(expected = OrderStateException.class)
    public void expectExceptionOnEmptyOrderPrev() {
        TpvOrder newOrder = createNewOrder();
        TpvOrder order = orderService.findOne(newOrder.getId());

        orderService.handlePrevState(order);
    }

    @Test(expected = OrderStateException.class)
    public void expectExceptionOnCompleteOrderNext() {
        TpvOrder newOrder = orderService.createNew();
        newOrder.setState(OrderState.COMPLETE);

        orderService.save(newOrder);

        TpvOrder order = orderService.findOne(newOrder.getId());

        orderService.handleNextState(order);
    }

    @Test(expected = OrderStateException.class)
    public void expectExceptionOnCompleteOrderPrev() {
        TpvOrder newOrder = orderService.createNew();
        newOrder.setState(OrderState.COMPLETE);

        orderService.save(newOrder);

        TpvOrder order = orderService.findOne(newOrder.getId());

        orderService.handlePrevState(order);
    }

    @Test(expected = OrderStateException.class)
    public void expectExceptionOnReadyToCompleteCompleteOrderNext() {
        TpvOrder newOrder = orderService.createNew();
        newOrder.setState(OrderState.READY_TO_COMPLETE);

        orderService.save(newOrder);

        TpvOrder order = orderService.findOne(newOrder.getId());

        try {
            orderService.handleNextState(order);
        } finally {
            assertThat(order.getState()).isEqualTo(OrderState.EMPTY);
        }
    }

    @Test(expected = OrderStateException.class)
    public void expectExceptionOnReadyToCompleteOrderPrev() {
        TpvOrder newOrder = createReadyToCompleteOrder();
        TpvOrder order = orderService.findOne(newOrder.getId());

        orderService.handlePrevState(order);
    }

    @Test
    public void expectToBeEmtyOnReadyToCompleteOrderPrev() {
        TpvOrder newOrder = createReadyToCompleteOrder();
        TpvOrder order = orderService.findOne(newOrder.getId());
        order.getLines().clear();
        orderService.save(order);
        order = orderService.findOne(newOrder.getId());

        orderService.handlePrevState(order);

        assertThat(order.getState()).isEqualTo(OrderState.EMPTY);
    }

    @Test
    public void expectToBeReadyToCompleteAndThenCompleteOnEmptyOrderNext() {
        TpvOrder newOrder = createNewOrder();
        newOrder.getLines().add(createLine());

        orderService.save(newOrder);

        TpvOrder order = orderService.findOne(newOrder.getId());

        orderService.handleNextState(order);

        assertThat(order.getState()).isEqualTo(OrderState.READY_TO_COMPLETE);

        orderService.handleNextState(order);

        assertThat(order.getState()).isEqualTo(OrderState.COMPLETE);
    }

    private TpvOrder createNewOrder() {
        TpvOrder newOrder = orderService.createNew();

        orderService.save(newOrder);
        return newOrder;
    }

    private TpvOrder createReadyToCompleteOrder() {
        TpvOrder newOrder = orderService.createNew();
        TpvOrderLine line = createLine();
        newOrder.getLines().add(line);
        newOrder.setState(OrderState.READY_TO_COMPLETE);
        orderService.save(newOrder);
        return newOrder;
    }

    private TpvOrderLine createLine() {
        TpvOrderLine line = new TpvOrderLine();
        line.setLineNumber(1);
        line.setQty(1);
        return line;
    }
}
