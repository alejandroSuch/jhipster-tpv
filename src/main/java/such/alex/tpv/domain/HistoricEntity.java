package such.alex.tpv.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
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

    @PrePersist
    public void prePersist() {
        if(active == null) {
            active = Boolean.TRUE;
        }

        if(activeFrom == null) {
            activeFrom = LocalDate.now();
            activeTo = null;
        }

        if(Boolean.FALSE.equals(active) && activeTo == null) {
            activeTo = LocalDate.now();
        }
    }
}
