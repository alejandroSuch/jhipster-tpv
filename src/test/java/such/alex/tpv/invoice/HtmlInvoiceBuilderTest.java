package such.alex.tpv.invoice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import such.alex.tpv.Application;
import such.alex.tpv.domain.*;
import such.alex.tpv.invoice.impl.HtmlInvoiceBuilder;
import such.alex.tpv.invoice.impl.PdfInvoiceBuilder;
import such.alex.tpv.repository.*;
import such.alex.tpv.service.TpvOrderService;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by alejandro on 23/02/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class HtmlInvoiceBuilderTest {
    @Inject
    DiscountRepository discountRepository;

    @Inject
    PriceRepository priceRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    private VatRepository vatRepository;

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    TpvOrderRepository tpvOrderRepository;

    @Inject
    private TpvOrderService tpvOrderService;

    @Inject
    private HtmlInvoiceBuilder htmlInvoiceBuilder;

    @Inject
    private PdfInvoiceBuilder pdfInvoiceBuilder;

    private TpvOrder order;

    @Before
    public void initTest() {
        final Discount discount = discountRepository.saveAndFlush(
            new Discount()
                .setActiveFrom(LocalDate.now().minusDays(1))
                .setActiveTo(LocalDate.now().plusDays(1))
                .setCode("discount001")
                .setDescription("discount001")
                .setUnits(2)
                .setValue(50f)
        );


        final Price price = priceRepository.saveAndFlush(new Price().setValue(120f));
        final Price price2 = priceRepository.saveAndFlush(new Price().setValue(120f));

        final Vat vat1 = vatRepository.saveAndFlush(
            new Vat()
                .setCode("VAT001")
                .setDescription("VAT001")
                .setValue(0.21f)
        );

        final Vat vat2 = vatRepository.saveAndFlush(
            new Vat()
                .setCode("VAT002")
                .setDescription("VAT002")
                .setValue(0.1f)
        );

        final Category cat1 = categoryRepository.saveAndFlush(
            new Category()
                .setName("CAT001")
                .setDescription("CAT001")
                .setVat(vat1)
        );

        final Category cat2 = categoryRepository.saveAndFlush(
            new Category()
                .setName("CAT002")
                .setDescription("CAT002")
                .setVat(vat2)
        );

        final Product product = productRepository.saveAndFlush(
            new Product()
                .setCode("1111111111111")
                .setDescription("Product001")
                .setName("Product001")
                .setPrice(price)
                .setCategory(cat1)
                .addDiscount(discount)
        );

        final Product product2 = productRepository.saveAndFlush(
            new Product()
                .setCode("1111111111112")
                .setDescription("Product002")
                .setName("Product002")
                .setPrice(price2)
                .setCategory(cat2)
        );

        order = new TpvOrder();

        tpvOrderService.addProduct(order, product);
        tpvOrderService.addProduct(order, product);
        tpvOrderService.addProduct(order, product);
        tpvOrderService.addProduct(order, product2);
        tpvOrderService.addProduct(order, product2);
        tpvOrderService.addProduct(order, product2);

        tpvOrderRepository.saveAndFlush(order);
    }

    @Test
    public void generatesAnHTMLDocument() {
        final String invoice = new String(htmlInvoiceBuilder.getInvoice(order));
        assertThat(invoice).isNotNull();
    }

    @Test
    public void generatesAPDFDocument() throws IOException {
        final byte[] data = pdfInvoiceBuilder.getInvoice(order);

        assertThat(data).isNotNull();

        assertThat(data[0]).isEqualTo((byte) 0x25); // %
        assertThat(data[1]).isEqualTo((byte) 0x50); // P
        assertThat(data[2]).isEqualTo((byte) 0x44); // D
        assertThat(data[3]).isEqualTo((byte) 0x46); // F
        assertThat(data[4]).isEqualTo((byte) 0x2D); // -

        if (data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x33) { // version is 1.3 ?
            // file terminator
            assertThat(data[data.length - 7]).isEqualTo((byte) 0x25); // %
            assertThat(data[data.length - 6]).isEqualTo((byte) 0x25); // %
            assertThat(data[data.length - 5]).isEqualTo((byte) 0x45); // E
            assertThat(data[data.length - 4]).isEqualTo((byte) 0x4F); // O
            assertThat(data[data.length - 3]).isEqualTo((byte) 0x46); // F
            assertThat(data[data.length - 2]).isEqualTo((byte) 0x20); // SPACE
            assertThat(data[data.length - 1]).isEqualTo((byte) 0x0A); // EOL
        } else if (data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x34) {// version is 1.4 ?
            // file terminator
            assertThat(data[data.length - 6]).isEqualTo((byte) 0x25); // %
            assertThat(data[data.length - 5]).isEqualTo((byte) 0x25); // %
            assertThat(data[data.length - 4]).isEqualTo((byte) 0x45); // E
            assertThat(data[data.length - 3]).isEqualTo((byte) 0x4F); // O
            assertThat(data[data.length - 2]).isEqualTo((byte) 0x46); // F
            assertThat(data[data.length - 1]).isEqualTo((byte) 0x0A); // EOL
        } else {
            Assert.fail("Unsupported format");
        }
    }
}


