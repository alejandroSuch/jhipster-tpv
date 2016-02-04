package such.alex.tpv.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by alejandro on 31/01/2016.
 */
@MappedSuperclass
public abstract class HistoricEntity implements Serializable, Cloneable {
    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "active_from", nullable = true)
    private LocalDate activeFrom;

    @Column(name = "active_to", nullable = true)
    private LocalDate activeTo;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public abstract HistoricEntity clone();

    public LocalDate getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(LocalDate activeFrom) {
        this.activeFrom = activeFrom;
    }

    public LocalDate getActiveTo() {
        return activeTo;
    }

    public void setActiveTo(LocalDate activeTo) {
        this.activeTo = activeTo;
    }
}
