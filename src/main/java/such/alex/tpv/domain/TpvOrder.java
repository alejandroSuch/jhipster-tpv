package such.alex.tpv.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import such.alex.tpv.state.order.OrderState;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * A TpvOrder.
 */
@Entity
@Table(name = "tpv_order")
@Document(indexName = "tpvorder")
public class TpvOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private ZonedDateTime dateCreated;

    @OneToMany(mappedBy = "tpvOrder", cascade = CascadeType.ALL)
    private Collection<TpvOrderLine> lines;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OrderState state;

    public TpvOrder() {
        this.state = OrderState.EMPTY;
        this.dateCreated = ZonedDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Collection<TpvOrderLine> getLines() {
        if(lines == null) {
            lines = new ArrayList<>();
        }

        return lines;
    }

    public void setLines(Collection<TpvOrderLine> lines) {
        this.lines = lines;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TpvOrder tpvOrder = (TpvOrder) o;
        if (tpvOrder.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tpvOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TpvOrder{" +
            "id=" + id +
            ", dateCreated='" + dateCreated + "'" +
            '}';
    }
}
