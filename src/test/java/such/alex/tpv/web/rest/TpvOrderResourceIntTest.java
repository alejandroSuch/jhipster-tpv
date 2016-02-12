package such.alex.tpv.web.rest;

import such.alex.tpv.Application;
import such.alex.tpv.domain.*;
import such.alex.tpv.repository.*;
import such.alex.tpv.service.ProductService;
import such.alex.tpv.service.TpvOrderService;

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
import such.alex.tpv.state.order.OrderState;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TpvOrderResource REST controller.
 *
 * @see TpvOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TpvOrderResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_CREATED_STR = dateTimeFormatter.format(DEFAULT_DATE_CREATED);
    public static final String CODE_1 = "1111111111111";
    public static final String CODE_2 = "1111111111112";

    @Inject
    private TpvOrderRepository tpvOrderRepository;

    @Inject
    private TpvOrderService tpvOrderService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTpvOrderMockMvc;

    private TpvOrder tpvOrder;

    @Inject
    private DiscountRepository discountRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private PriceRepository priceRepository;

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    private VatRepository vatRepository;

    @Inject
    ProductService productService;


    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TpvOrderResource tpvOrderResource = new TpvOrderResource();
        ReflectionTestUtils.setField(tpvOrderResource, "tpvOrderService", tpvOrderService);
        this.restTpvOrderMockMvc = MockMvcBuilders.standaloneSetup(tpvOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tpvOrder = new TpvOrder();
        tpvOrder.setDateCreated(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    public void createTpvOrder() throws Exception {
        int databaseSizeBeforeCreate = tpvOrderRepository.findAll().size();

        // Create the TpvOrder

        restTpvOrderMockMvc.perform(post("/api/tpvOrders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tpvOrder)))
            .andExpect(status().isCreated());

        // Validate the TpvOrder in the database
        List<TpvOrder> tpvOrders = tpvOrderRepository.findAll();
        assertThat(tpvOrders).hasSize(databaseSizeBeforeCreate + 1);
        TpvOrder testTpvOrder = tpvOrders.get(tpvOrders.size() - 1);
        assertThat(testTpvOrder.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllTpvOrders() throws Exception {
        // Initialize the database
        tpvOrderRepository.saveAndFlush(tpvOrder);

        // Get all the tpvOrders
        restTpvOrderMockMvc.perform(get("/api/tpvOrders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tpvOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED_STR)));
    }

    @Test
    @Transactional
    public void getTpvOrder() throws Exception {
        // Initialize the database
        tpvOrderRepository.saveAndFlush(tpvOrder);

        // Get the tpvOrder
        restTpvOrderMockMvc.perform(get("/api/tpvOrders/{id}", tpvOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tpvOrder.getId().intValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTpvOrder() throws Exception {
        // Get the tpvOrder
        restTpvOrderMockMvc.perform(get("/api/tpvOrders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTpvOrder() throws Exception {
        // Initialize the database
        tpvOrderRepository.saveAndFlush(tpvOrder);

        int databaseSizeBeforeUpdate = tpvOrderRepository.findAll().size();

        // Update the tpvOrder
        tpvOrder.setDateCreated(UPDATED_DATE_CREATED);

        restTpvOrderMockMvc.perform(put("/api/tpvOrders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tpvOrder)))
            .andExpect(status().isOk());

        // Validate the TpvOrder in the database
        List<TpvOrder> tpvOrders = tpvOrderRepository.findAll();
        assertThat(tpvOrders).hasSize(databaseSizeBeforeUpdate);
        TpvOrder testTpvOrder = tpvOrders.get(tpvOrders.size() - 1);
        assertThat(testTpvOrder.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void deleteTpvOrder() throws Exception {
        // Initialize the database
        tpvOrderRepository.saveAndFlush(tpvOrder);

        int databaseSizeBeforeDelete = tpvOrderRepository.findAll().size();

        // Get the tpvOrder
        restTpvOrderMockMvc.perform(delete("/api/tpvOrders/{id}", tpvOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TpvOrder> tpvOrders = tpvOrderRepository.findAll();
        assertThat(tpvOrders).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void someFlowsThatWork() {
        TpvOrder order = new TpvOrder();

        initData();


        final Product product001 = productService.findByCode(CODE_1);
        final Product product002 = productService.findByCode(CODE_2);

        order = tpvOrderService.save(order);
        assertThat(order.getState()).isEqualTo(OrderState.EMPTY);

        order = tpvOrderService.addProduct(order, product001);
        assertThat(order.getState()).isEqualTo(OrderState.READY_TO_COMPLETE);
        order = tpvOrderService.removeProduct(order, product001);
        assertThat(order.getState()).isEqualTo(OrderState.EMPTY);
        order = tpvOrderService.addProduct(order, product001);
        order = tpvOrderService.addProduct(order, product001);
        assertThat(order.getLines()).isNotEmpty();
        order = tpvOrderService.addProduct(order, product002);
        int i = 0;
    }

    private void initData() {
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

        final Vat vat = vatRepository.saveAndFlush(
            new Vat()
                .setCode("VAT001")
                .setDescription("VAT001")
                .setValue(0.21f)
        );

        final Category category = categoryRepository.saveAndFlush(
            new Category()
                .setDescription("CAT001")
                .setName("CAT001")
                .setVat(vat)
        );

        final Product product = productRepository.saveAndFlush(
            new Product()
                .setCode(CODE_1)
                .setDescription("Product001")
                .setName("Product001")
                .setPrice(price)
                .setCategory(category)
                .addDiscount(discount)
        );

        final Product product2 = productRepository.saveAndFlush(
            new Product()
                .setCode(CODE_2)
                .setDescription("Product002")
                .setName("Product002")
                .setPrice(price2)
                .setCategory(category)
        );
    }
}
