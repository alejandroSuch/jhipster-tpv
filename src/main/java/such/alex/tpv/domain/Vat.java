package such.alex.tpv.domain;

import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * A Vat.
 */
@Entity
@Table(name = "vat")
@Document(indexName = "vat")
public class Vat extends HistoricEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 4, max = 8)
    @Column(name = "code", length = 8, nullable = false)
    private String code;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "value", nullable = false)
    private Float value;

    public Long getId() {
        return id;
    }

    public Vat setId(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Vat setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Vat setDescription(String description) {
        this.description = description;
        return this;
    }

    public Float getValue() {
        return value;
    }

    public Vat setValue(Float value) {
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
        Vat vat = (Vat) o;
        if (vat.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, vat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Vat{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            ", value='" + value + "'" +
            '}';
    }

    public Vat clone() {
        Vat result = new Vat();
        BeanUtils.copyProperties(this, result, "id", "active", "activeFrom", "activeTo");
        return result;
    }
}
