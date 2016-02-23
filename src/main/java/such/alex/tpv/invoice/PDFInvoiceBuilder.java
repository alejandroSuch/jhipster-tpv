package such.alex.tpv.invoice;

import com.itextpdf.text.DocumentException;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import such.alex.tpv.domain.TpvOrder;

import java.io.IOException;

/**
 * Created by alejandro on 19/02/2016.
 */
@Service
public class PDFInvoiceBuilder {
    @Autowired
    TemplateEngine templateEngine;

    public byte[] getInvoice(TpvOrder order) {
        Context ctx = new Context();

        ctx.setVariable("order", order);
        String htmlContent = templateEngine.process("invoice/invoice-pdf", ctx);

        ITextRenderer renderer = new ITextRenderer();

        ByteOutputStream os = new ByteOutputStream();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        try {
            renderer.createPDF(os);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] pdfAsBytes = os.getBytes();
        os.close();

        return pdfAsBytes;
    }
}
