package such.alex.tpv.domain;

import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Discount.
 */
@Entity
@Table(name = "discount")
@Document(indexName = "discount")
public class Discount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Min(value = 1)
    @Column(name = "value", nullable = false)
    private Float value;

    @NotNull
    @Min(value = 0)
    @Column(name = "units", nullable = false)
    private Integer units;

    @NotNull
    @Column(name = "active_from", nullable = false)
    private LocalDate activeFrom;

    @NotNull
    @Column(name = "active_to", nullable = false)
    private LocalDate activeTo;

    public Long getId() {
        return id;
    }

    public Discount setId(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Discount setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Discount setDescription(String description) {
        this.description = description;
        return this;
    }

    public Float getValue() {
        return value;
    }

    public Discount setValue(Float value) {
        this.value = value;
        return this;
    }

    public Integer getUnits() {
        return units;
    }

    public Discount setUnits(Integer units) {
        this.units = units;
        return this;
    }

    public LocalDate getActiveFrom() {
        return activeFrom;
    }

    public Discount setActiveFrom(LocalDate activeFrom) {
        this.activeFrom = activeFrom;
        return this;
    }

    public LocalDate getActiveTo() {
        return activeTo;
    }

    public Discount setActiveTo(LocalDate activeTo) {
        this.activeTo = activeTo;
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
        Discount discount = (Discount) o;
        if(discount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, discount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Discount{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            ", value='" + value + "'" +
            ", units='" + units + "'" +
            ", activeFrom='" + activeFrom + "'" +
            ", activeTo='" + activeTo + "'" +
            '}';
    }
}
