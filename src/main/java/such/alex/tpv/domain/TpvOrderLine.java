package such.alex.tpv.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TpvOrderLine.
 */
@Entity
@Table(name = "tpv_order_line")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Document(indexName = "tpvorderline")
@DiscriminatorColumn(name = "TYPE", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("ORDER_LINE")
public class TpvOrderLine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
    
    @NotNull
    @Min(value = 1)
    @Column(name = "qty", nullable = false)
    private Integer qty;
    
    @ManyToOne
    @JoinColumn(name = "tpv_order_id")
    private TpvOrder tpvOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "price_id")
    private Price price;

    @ManyToOne
    @JoinColumn(name = "vat_id")
    private Vat vat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }
    
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Integer getQty() {
        return qty;
    }
    
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public TpvOrder getTpvOrder() {
        return tpvOrder;
    }

    public void setTpvOrder(TpvOrder tpvOrder) {
        this.tpvOrder = tpvOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Vat getVat() {
        return vat;
    }

    public void setVat(Vat vat) {
        this.vat = vat;
    }

    @Transient
    public Float getTotal() {
        return getPrice().getValue() * getQty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TpvOrderLine tpvOrderLine = (TpvOrderLine) o;
        if(tpvOrderLine.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tpvOrderLine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TpvOrderLine{" +
            "id=" + id +
            ", lineNumber='" + lineNumber + "'" +
            ", qty='" + qty + "'" +
            '}';
    }
}
