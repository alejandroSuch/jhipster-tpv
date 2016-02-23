package such.alex.tpv.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TpvDiscountedOrderLine.
 */
@Entity
@Document(indexName = "tpvdiscountedorderline")
@DiscriminatorValue("DISCOUNTED_ORDER_LINE")
public class TpvDiscountedOrderLine extends TpvOrderLine {

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    public TpvDiscountedOrderLine(){

    }

    public TpvDiscountedOrderLine(Discount discount) {
        this.discount = discount;
    }

    public Discount getDiscount() {
        return discount;
    }

    public TpvDiscountedOrderLine setDiscount(Discount discount) {
        this.discount = discount;
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
        TpvDiscountedOrderLine tpvDiscountedOrderLine = (TpvDiscountedOrderLine) o;
        if(tpvDiscountedOrderLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(this.getId(), tpvDiscountedOrderLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    @Override
    public String toString() {
        return "TpvDiscountedOrderLine{" +
            "id=" + getId() +
            '}';
    }

    @Override
    @Transient
    public Float getSubtotal() {
        final Float price = getPrice().getValue();
        final int unitsToApplyDiscount = Math.round(getDiscount().getUnits() % getQty());
        final int unitsNotToApplyDiscount = this.getQty() - unitsToApplyDiscount;

        final Float discountValue = getDiscount().getValue() / 100;
        return
            (price * unitsNotToApplyDiscount) +
            ((1 - discountValue) * price * unitsToApplyDiscount);
    }

    @Override
    @Transient
    public String getDescription() {
        return new StringBuilder()
            .append(this.getProduct().getName())
            .append(" (")
            .append(this.getDiscount().getCode())
            .append(")")
            .toString();
    }
}
