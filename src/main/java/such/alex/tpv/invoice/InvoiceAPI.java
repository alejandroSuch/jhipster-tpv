package such.alex.tpv.invoice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import such.alex.tpv.domain.TpvOrder;

import javax.inject.Inject;

/**
 * Created by alejandro on 05/04/2016.
 */
@Service
public class InvoiceAPI {
    @Inject
    InvoiceBuilder htmlInvoiceBuilder;

    @Inject
    InvoiceBuilder pdfInvoiceBuilder;

    @Transactional(readOnly = true)
    public byte[] getInvoiceAsPdf(TpvOrder order) {
        return pdfInvoiceBuilder.getInvoice(order);
    }

    @Transactional(readOnly = true)
    public String getInvoiceAsHtml(TpvOrder order) {
        return new String(htmlInvoiceBuilder.getInvoice(order));
    }
}
