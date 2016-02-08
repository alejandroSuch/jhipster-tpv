package such.alex.tpv.state.order.lambdas;

import such.alex.tpv.domain.TpvOrder;
import such.alex.tpv.state.order.OrderState;
import such.alex.tpv.state.order.OrderStateException;

import java.util.function.Function;

/**
 * Created by alejandro on 08/02/2016.
 */
public class EmptyStateLambdas {
    public static Function<TpvOrder, Void> prev() {
        return tpvOrder -> {
            throw new OrderStateException("Order has no previous state");
        };
    }

    public static Function<TpvOrder, Void> next() {
        return tpvOrder -> {
            if (tpvOrder.getLines().size() == 0) {
                throw new OrderStateException("Order has no lines");
            }

            tpvOrder.setState(OrderState.READY_TO_COMPLETE);
            return null;
        };
    }
}
