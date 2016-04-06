package such.alex.tpv.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import such.alex.tpv.domain.TpvOrder;
import such.alex.tpv.invoice.InvoiceAPI;
import such.alex.tpv.service.TpvOrderService;

import javax.inject.Inject;

/**
 * Created by alejandro on 05/04/2016.
 */
@Controller
@RequestMapping("/invoice/view")
public class InvoiceController {
    public static final String MEDIATYPE_APPLICATION_PDF = "application/pdf";

    @Inject
    TpvOrderService tpvOrderService;

    @Inject
    InvoiceAPI invoiceAPI;

    @Transactional(readOnly = true)
    @RequestMapping(
        value = "/pdf/{orderId}",
        produces = MEDIATYPE_APPLICATION_PDF,
        method = RequestMethod.GET
    )
    public ResponseEntity<byte[]> asPdf(@PathVariable Long orderId) {
        final TpvOrder order = tpvOrderService.findOne(orderId);

        if(order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(invoiceAPI.getInvoiceAsPdf(order), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @RequestMapping(
        value = "/html/{orderId}",
        produces = MEDIATYPE_APPLICATION_PDF,
        method = RequestMethod.GET
    )
    public ResponseEntity<String> asHtml(@PathVariable Long orderId) {
        final TpvOrder order = tpvOrderService.findOne(orderId);

        if(order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(invoiceAPI.getInvoiceAsHtml(order), HttpStatus.OK);
    }
}

