package such.alex.tpv.state.order;

/**
 * Created by alejandro on 08/02/2016.
 */
public class OrderStateException extends RuntimeException {
    public OrderStateException() {
        super();
    }

    public OrderStateException(String message) {
        super(message);
    }

    public OrderStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
