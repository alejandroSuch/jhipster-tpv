package such.alex.tpv.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import such.alex.tpv.domain.TpvOrder;

/**
 * Created by alejandro on 19/02/2016.
 */
@Service
public class HTMLInvoiceBuilder {
    @Autowired
    TemplateEngine templateEngine;

    public String getInvoice(TpvOrder order) {
        Context ctx = new Context();

        ctx.setVariable("order", order);
        String htmlContent = templateEngine.process("invoice/invoice-html", ctx);

        return htmlContent;
    }
}
