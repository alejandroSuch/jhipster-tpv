package such.alex.tpv.service;

/**
 * Created by alejandro on 12/2/16.
 */
public class TpvOrderException extends RuntimeException {

    public TpvOrderException() {
        super();
    }

    public TpvOrderException(String message) {
        super(message);
    }

    public TpvOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
