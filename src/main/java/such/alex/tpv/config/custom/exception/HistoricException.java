package such.alex.tpv.config.custom.exception;

/**
 * Created by alejandro on 4/2/16.
 */
public class HistoricException extends RuntimeException {
    public HistoricException() {
        super();
    }

    public HistoricException(String message) {
        super(message);
    }

    public HistoricException(String message, Throwable cause) {
        super(message, cause);
    }
}
