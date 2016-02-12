package such.alex.tpv.domain;

import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Price.
 */
@Entity
@Table(name = "price")
@Document(indexName = "price")
public class Price extends HistoricEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "value", nullable = false)
    private Float value;

    public Long getId() {
        return id;
    }

    public Price setId(Long id) {
        this.id = id;
        return this;
    }

    public Float getValue() {
        return value;
    }

    public Price setValue(Float value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        if(price.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, price.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Price{" +
            "id=" + id +
            ", value='" + value + "'" +
            '}';
    }

    @Override
    public Price clone() {
        Price result = new Price();
        BeanUtils.copyProperties(this, result, "id", "activeFrom", "activeTo", "active");
        return result;
    }
}
