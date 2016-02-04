package such.alex.tpv.config.custom.exception;

/**
 * Created by alejandro on 4/2/16.
 */
public class HistoricRepositoryException extends RuntimeException {
    public HistoricRepositoryException() {
        super();
    }

    public HistoricRepositoryException(String message) {
        super(message);
    }

    public HistoricRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
