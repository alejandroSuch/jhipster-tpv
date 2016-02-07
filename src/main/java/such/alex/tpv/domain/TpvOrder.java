package such.alex.tpv.domain;

import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TpvOrder tpvOrder = (TpvOrder) o;
        if(tpvOrder.id == null || id == null) {
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
