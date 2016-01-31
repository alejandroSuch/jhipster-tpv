package such.alex.tpv.domain;

import java.io.Serializable;

/**
 * Created by alejandro on 31/01/2016.
 */
public abstract class HistoricEntity implements Serializable, Cloneable {
    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public abstract HistoricEntity clone();
}
