package such.alex.tpv.web.rest;

import such.alex.tpv.Application;
import such.alex.tpv.domain.Discount;
import such.alex.tpv.domain.Price;
import such.alex.tpv.domain.Product;
import such.alex.tpv.domain.TpvDiscountedOrderLine;
import such.alex.tpv.repository.DiscountRepository;
import such.alex.tpv.repository.PriceRepository;
import such.alex.tpv.repository.ProductRepository;
import such.alex.tpv.repository.TpvDiscountedOrderLineRepository;
import such.alex.tpv.service.TpvDiscountedOrderLineService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasItem;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TpvDiscountedOrderLineResource REST controller.
 *
 * @see TpvDiscountedOrderLineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TpvDiscountedOrderLineResourceIntTest {


    @Inject
    private TpvDiscountedOrderLineRepository tpvDiscountedOrderLineRepository;

    @Inject
    private TpvDiscountedOrderLineService tpvDiscountedOrderLineService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTpvDiscountedOrderLineMockMvc;

    private TpvDiscountedOrderLine tpvDiscountedOrderLine;

    @Inject
    DiscountRepository discountRepository;

    @Inject
    PriceRepository priceRepository;

    @Inject
    ProductRepository productRepository;


    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TpvDiscountedOrderLineResource tpvDiscountedOrderLineResource = new TpvDiscountedOrderLineResource();
        ReflectionTestUtils.setField(tpvDiscountedOrderLineResource, "tpvDiscountedOrderLineService", tpvDiscountedOrderLineService);
        this.restTpvDiscountedOrderLineMockMvc = MockMvcBuilders.standaloneSetup(tpvDiscountedOrderLineResource)
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

        final Product product = productRepository.saveAndFlush(
            new Product()
                .setCode("1111111111111")
                .setDescription("Product001")
                .setName("Product001")
                .setPrice(price)
        );

        tpvDiscountedOrderLine = (TpvDiscountedOrderLine) new TpvDiscountedOrderLine()
            .setDiscount(discount)
            .setProduct(product)
            .setPrice(product.getPrice())
            .setLineNumber(1)
            .setQty(1);
    }

    @Test
    @Transactional
    public void createTpvDiscountedOrderLine() throws Exception {
        int databaseSizeBeforeCreate = tpvDiscountedOrderLineRepository.findAll().size();

        // Create the TpvDiscountedOrderLine

        restTpvDiscountedOrderLineMockMvc.perform(post("/api/tpvDiscountedOrderLines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tpvDiscountedOrderLine)))
            .andExpect(status().isCreated());

        // Validate the TpvDiscountedOrderLine in the database
        List<TpvDiscountedOrderLine> tpvDiscountedOrderLines = tpvDiscountedOrderLineRepository.findAll();
        assertThat(tpvDiscountedOrderLines).hasSize(databaseSizeBeforeCreate + 1);
        TpvDiscountedOrderLine testTpvDiscountedOrderLine = tpvDiscountedOrderLines.get(tpvDiscountedOrderLines.size() - 1);
    }

    @Test
    @Transactional
    public void getAllTpvDiscountedOrderLines() throws Exception {
        // Initialize the database
        tpvDiscountedOrderLineRepository.saveAndFlush(tpvDiscountedOrderLine);

        // Get all the tpvDiscountedOrderLines
        restTpvDiscountedOrderLineMockMvc.perform(get("/api/tpvDiscountedOrderLines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tpvDiscountedOrderLine.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTpvDiscountedOrderLine() throws Exception {
        // Initialize the database
        tpvDiscountedOrderLineRepository.saveAndFlush(tpvDiscountedOrderLine);

        // Get the tpvDiscountedOrderLine
        restTpvDiscountedOrderLineMockMvc.perform(get("/api/tpvDiscountedOrderLines/{id}", tpvDiscountedOrderLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tpvDiscountedOrderLine.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTpvDiscountedOrderLine() throws Exception {
        // Get the tpvDiscountedOrderLine
        restTpvDiscountedOrderLineMockMvc.perform(get("/api/tpvDiscountedOrderLines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTpvDiscountedOrderLine() throws Exception {
        // Initialize the database
        tpvDiscountedOrderLineRepository.saveAndFlush(tpvDiscountedOrderLine);

        int databaseSizeBeforeUpdate = tpvDiscountedOrderLineRepository.findAll().size();

        // Update the tpvDiscountedOrderLine

        restTpvDiscountedOrderLineMockMvc.perform(put("/api/tpvDiscountedOrderLines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tpvDiscountedOrderLine)))
            .andExpect(status().isOk());

        // Validate the TpvDiscountedOrderLine in the database
        List<TpvDiscountedOrderLine> tpvDiscountedOrderLines = tpvDiscountedOrderLineRepository.findAll();
        assertThat(tpvDiscountedOrderLines).hasSize(databaseSizeBeforeUpdate);
        TpvDiscountedOrderLine testTpvDiscountedOrderLine = tpvDiscountedOrderLines.get(tpvDiscountedOrderLines.size() - 1);
    }

    @Test
    @Transactional
    public void deleteTpvDiscountedOrderLine() throws Exception {
        // Initialize the database
        tpvDiscountedOrderLineRepository.saveAndFlush(tpvDiscountedOrderLine);

        int databaseSizeBeforeDelete = tpvDiscountedOrderLineRepository.findAll().size();

        // Get the tpvDiscountedOrderLine
        restTpvDiscountedOrderLineMockMvc.perform(delete("/api/tpvDiscountedOrderLines/{id}", tpvDiscountedOrderLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TpvDiscountedOrderLine> tpvDiscountedOrderLines = tpvDiscountedOrderLineRepository.findAll();
        assertThat(tpvDiscountedOrderLines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
