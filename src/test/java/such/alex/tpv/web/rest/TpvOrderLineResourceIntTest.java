package such.alex.tpv.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import such.alex.tpv.Application;
import such.alex.tpv.domain.*;
import such.alex.tpv.repository.*;
import such.alex.tpv.service.TpvOrderLineService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TpvOrderLineResource REST controller.
 *
 * @see TpvOrderLineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TpvOrderLineResourceIntTest {


    private static final Integer DEFAULT_LINE_NUMBER = 1;
    private static final Integer UPDATED_LINE_NUMBER = 2;

    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;

    @Inject
    private TpvOrderLineRepository tpvOrderLineRepository;

    @Inject
    private TpvOrderLineService tpvOrderLineService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTpvOrderLineMockMvc;

    private TpvOrderLine tpvOrderLine;

    @Inject
    DiscountRepository discountRepository;

    @Inject
    PriceRepository priceRepository;

    @Inject
    VatRepository vatRepository;

    @Inject
    ProductRepository productRepository;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TpvOrderLineResource tpvOrderLineResource = new TpvOrderLineResource();
        ReflectionTestUtils.setField(tpvOrderLineResource, "tpvOrderLineService", tpvOrderLineService);
        this.restTpvOrderLineMockMvc = MockMvcBuilders.standaloneSetup(tpvOrderLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

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
        final Vat vat = vatRepository.saveAndFlush(new Vat().setCode("theVAT").setDescription("theVAT").setValue(0.2f));

        final Product product = productRepository.saveAndFlush(
            new Product()
                .setCode("1111111111111")
                .setDescription("Product001")
                .setName("Product001")
                .setPrice(price)
        );

        tpvOrderLine = new TpvOrderLine()
            .setLineNumber(DEFAULT_LINE_NUMBER)
            .setQty(DEFAULT_QTY)
            .setProduct(product)
            .setVat(vat)
            .setPrice(product.getPrice());
    }

    @Test
    @Transactional
    public void createTpvOrderLine() throws Exception {
        int databaseSizeBeforeCreate = tpvOrderLineRepository.findAll().size();

        // Create the TpvOrderLine

        restTpvOrderLineMockMvc.perform(post("/api/tpvOrderLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tpvOrderLine)))
                .andExpect(status().isCreated());

        // Validate the TpvOrderLine in the database
        List<TpvOrderLine> tpvOrderLines = tpvOrderLineRepository.findAll();
        assertThat(tpvOrderLines).hasSize(databaseSizeBeforeCreate + 1);
        TpvOrderLine testTpvOrderLine = tpvOrderLines.get(tpvOrderLines.size() - 1);
        assertThat(testTpvOrderLine.getLineNumber()).isEqualTo(DEFAULT_LINE_NUMBER);
        assertThat(testTpvOrderLine.getQty()).isEqualTo(DEFAULT_QTY);
    }

    @Test
    @Transactional
    public void checkLineNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = tpvOrderLineRepository.findAll().size();
        // set the field null
        tpvOrderLine.setLineNumber(null);

        // Create the TpvOrderLine, which fails.

        restTpvOrderLineMockMvc.perform(post("/api/tpvOrderLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tpvOrderLine)))
                .andExpect(status().isBadRequest());

        List<TpvOrderLine> tpvOrderLines = tpvOrderLineRepository.findAll();
        assertThat(tpvOrderLines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = tpvOrderLineRepository.findAll().size();
        // set the field null
        tpvOrderLine.setQty(null);

        // Create the TpvOrderLine, which fails.

        restTpvOrderLineMockMvc.perform(post("/api/tpvOrderLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tpvOrderLine)))
                .andExpect(status().isBadRequest());

        List<TpvOrderLine> tpvOrderLines = tpvOrderLineRepository.findAll();
        assertThat(tpvOrderLines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTpvOrderLines() throws Exception {
        // Initialize the database
        tpvOrderLineRepository.saveAndFlush(tpvOrderLine);

        // Get all the tpvOrderLines
        restTpvOrderLineMockMvc.perform(get("/api/tpvOrderLines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tpvOrderLine.getId().intValue())))
                .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
                .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)));
    }

    @Test
    @Transactional
    public void getTpvOrderLine() throws Exception {
        // Initialize the database
        tpvOrderLineRepository.saveAndFlush(tpvOrderLine);

        // Get the tpvOrderLine
        restTpvOrderLineMockMvc.perform(get("/api/tpvOrderLines/{id}", tpvOrderLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tpvOrderLine.getId().intValue()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY));
    }

    @Test
    @Transactional
    public void getNonExistingTpvOrderLine() throws Exception {
        // Get the tpvOrderLine
        restTpvOrderLineMockMvc.perform(get("/api/tpvOrderLines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTpvOrderLine() throws Exception {
        // Initialize the database
        tpvOrderLineRepository.saveAndFlush(tpvOrderLine);

		int databaseSizeBeforeUpdate = tpvOrderLineRepository.findAll().size();

        // Update the tpvOrderLine
        tpvOrderLine.setLineNumber(UPDATED_LINE_NUMBER);
        tpvOrderLine.setQty(UPDATED_QTY);

        restTpvOrderLineMockMvc.perform(put("/api/tpvOrderLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tpvOrderLine)))
                .andExpect(status().isOk());

        // Validate the TpvOrderLine in the database
        List<TpvOrderLine> tpvOrderLines = tpvOrderLineRepository.findAll();
        assertThat(tpvOrderLines).hasSize(databaseSizeBeforeUpdate);
        TpvOrderLine testTpvOrderLine = tpvOrderLines.get(tpvOrderLines.size() - 1);
        assertThat(testTpvOrderLine.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testTpvOrderLine.getQty()).isEqualTo(UPDATED_QTY);
    }

    @Test
    @Transactional
    public void deleteTpvOrderLine() throws Exception {
        // Initialize the database
        tpvOrderLineRepository.saveAndFlush(tpvOrderLine);

		int databaseSizeBeforeDelete = tpvOrderLineRepository.findAll().size();

        // Get the tpvOrderLine
        restTpvOrderLineMockMvc.perform(delete("/api/tpvOrderLines/{id}", tpvOrderLine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TpvOrderLine> tpvOrderLines = tpvOrderLineRepository.findAll();
        assertThat(tpvOrderLines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
