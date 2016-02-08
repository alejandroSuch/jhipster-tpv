package such.alex.tpv.state.order.lambdas;

import such.alex.tpv.domain.TpvOrder;
import such.alex.tpv.state.order.OrderState;
import such.alex.tpv.state.order.OrderStateException;

import java.util.function.Function;

/**
 * Created by alejandro on 08/02/2016.
 */
public class ReadyToCompleteStateLambdas {
    public static Function<TpvOrder, Void> prev() {
        return tpvOrder -> {
            if (tpvOrder.getLines().size() != 0) {
                throw new OrderStateException("Order must have zero lines");
            }

            tpvOrder.setState(OrderState.EMPTY);
            return null;
        };
    }

    public static Function<TpvOrder, Void> next() {
        return tpvOrder -> {
            if (tpvOrder.getLines().size() == 0) {
                tpvOrder.setState(OrderState.EMPTY);
                throw new OrderStateException("Order must have zero lines");
            }


            tpvOrder.setState(OrderState.COMPLETE);
            return null;
        };
    }

}
