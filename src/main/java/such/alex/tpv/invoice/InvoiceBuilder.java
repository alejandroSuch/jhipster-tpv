package such.alex.tpv.invoice;

import such.alex.tpv.domain.TpvOrder;

/**
 * Created by alejandro on 05/04/2016.
 */
public interface InvoiceBuilder {
    public byte[] getInvoice(TpvOrder order);
}
