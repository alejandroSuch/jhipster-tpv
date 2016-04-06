package such.alex.tpv.invoice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import such.alex.tpv.domain.TpvOrder;
import such.alex.tpv.invoice.InvoiceBuilder;

/**
 * Created by alejandro on 19/02/2016.
 */
@Service("htmlInvoiceBuilder")
public class HtmlInvoiceBuilder implements InvoiceBuilder {
    @Autowired
    TemplateEngine templateEngine;

    @Override
    @Transactional(readOnly = true)
    public byte[] getInvoice(TpvOrder order) {
        Context ctx = new Context();

        ctx.setVariable("order", order);
        String htmlContent = templateEngine.process("invoice/invoice-html", ctx);

        return htmlContent.getBytes();
    }
}
