package such.alex.tpv.state.order;

import such.alex.tpv.domain.TpvOrder;
import such.alex.tpv.state.order.lambdas.CompleteStateLambdas;
import such.alex.tpv.state.order.lambdas.EmptyStateLambdas;
import such.alex.tpv.state.order.lambdas.ReadyToCompleteStateLambdas;

import java.util.function.Function;

/**
 * Created by alejandro on 08/02/2016.
 */
public enum OrderState {
    COMPLETE(CompleteStateLambdas.next(), CompleteStateLambdas.prev()),
    READY_TO_COMPLETE(ReadyToCompleteStateLambdas.next(), ReadyToCompleteStateLambdas.prev()),
    EMPTY(EmptyStateLambdas.next(), EmptyStateLambdas.next());

    private Function<TpvOrder, Void> handleNextFn;
    private Function<TpvOrder, Void> handlePrevFn;

    OrderState(Function<TpvOrder, Void> handleNextFn, Function<TpvOrder, Void> handlePrevFn) {
        this.handleNextFn = handleNextFn;
        this.handlePrevFn = handlePrevFn;
    }

    public void handleNext(TpvOrder order) {
        this.handleNextFn.apply(order);
    }

    public void handlePrev(TpvOrder order) {
        this.handlePrevFn.apply(order);
    }

    private static class OrderStateFunctions {



    }
}
