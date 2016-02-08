package such.alex.tpv.state.order.lambdas;

import such.alex.tpv.domain.TpvOrder;
import such.alex.tpv.state.order.OrderStateException;

import java.util.function.Function;

/**
 * Created by alejandro on 08/02/2016.
 */
public class CompleteStateLambdas {
    public static Function<TpvOrder, Void> prev() {
        return tpvOrder -> {
            throw new OrderStateException("Order has finished");
        };
    }

    public static Function<TpvOrder, Void> next() {
        return tpvOrder -> {
            throw new OrderStateException("Order has finished");
        };
    }
}
