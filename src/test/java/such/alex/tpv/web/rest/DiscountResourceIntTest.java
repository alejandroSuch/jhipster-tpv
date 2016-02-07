package such.alex.tpv.web.rest;

import such.alex.tpv.Application;
import such.alex.tpv.domain.Discount;
import such.alex.tpv.repository.DiscountRepository;
import such.alex.tpv.service.DiscountService;

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
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DiscountResource REST controller.
 *
 * @see DiscountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DiscountResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Float DEFAULT_VALUE = 1F;
    private static final Float UPDATED_VALUE = 2F;

    private static final Integer DEFAULT_UNITS = 0;
    private static final Integer UPDATED_UNITS = 1;

    private static final LocalDate DEFAULT_ACTIVE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ACTIVE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVE_TO = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private DiscountRepository discountRepository;

    @Inject
    private DiscountService discountService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDiscountMockMvc;

    private Discount discount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiscountResource discountResource = new DiscountResource();
        ReflectionTestUtils.setField(discountResource, "discountService", discountService);
        this.restDiscountMockMvc = MockMvcBuilders.standaloneSetup(discountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        discount = new Discount();
        discount.setCode(DEFAULT_CODE);
        discount.setDescription(DEFAULT_DESCRIPTION);
        discount.setValue(DEFAULT_VALUE);
        discount.setUnits(DEFAULT_UNITS);
        discount.setActiveFrom(DEFAULT_ACTIVE_FROM);
        discount.setActiveTo(DEFAULT_ACTIVE_TO);
    }

    @Test
    @Transactional
    public void createDiscount() throws Exception {
        int databaseSizeBeforeCreate = discountRepository.findAll().size();

        // Create the Discount

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isCreated());

        // Validate the Discount in the database
        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeCreate + 1);
        Discount testDiscount = discounts.get(discounts.size() - 1);
        assertThat(testDiscount.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDiscount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDiscount.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testDiscount.getUnits()).isEqualTo(DEFAULT_UNITS);
        assertThat(testDiscount.getActiveFrom()).isEqualTo(DEFAULT_ACTIVE_FROM);
        assertThat(testDiscount.getActiveTo()).isEqualTo(DEFAULT_ACTIVE_TO);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setCode(null);

        // Create the Discount, which fails.

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isBadRequest());

        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setDescription(null);

        // Create the Discount, which fails.

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isBadRequest());

        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setValue(null);

        // Create the Discount, which fails.

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isBadRequest());

        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setUnits(null);

        // Create the Discount, which fails.

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isBadRequest());

        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setActiveFrom(null);

        // Create the Discount, which fails.

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isBadRequest());

        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveToIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setActiveTo(null);

        // Create the Discount, which fails.

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isBadRequest());

        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscounts() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discounts
        restDiscountMockMvc.perform(get("/api/discounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].units").value(hasItem(DEFAULT_UNITS)))
                .andExpect(jsonPath("$.[*].activeFrom").value(hasItem(DEFAULT_ACTIVE_FROM.toString())))
                .andExpect(jsonPath("$.[*].activeTo").value(hasItem(DEFAULT_ACTIVE_TO.toString())));
    }

    @Test
    @Transactional
    public void getDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get the discount
        restDiscountMockMvc.perform(get("/api/discounts/{id}", discount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(discount.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.units").value(DEFAULT_UNITS))
            .andExpect(jsonPath("$.activeFrom").value(DEFAULT_ACTIVE_FROM.toString()))
            .andExpect(jsonPath("$.activeTo").value(DEFAULT_ACTIVE_TO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscount() throws Exception {
        // Get the discount
        restDiscountMockMvc.perform(get("/api/discounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

		int databaseSizeBeforeUpdate = discountRepository.findAll().size();

        // Update the discount
        discount.setCode(UPDATED_CODE);
        discount.setDescription(UPDATED_DESCRIPTION);
        discount.setValue(UPDATED_VALUE);
        discount.setUnits(UPDATED_UNITS);
        discount.setActiveFrom(UPDATED_ACTIVE_FROM);
        discount.setActiveTo(UPDATED_ACTIVE_TO);

        restDiscountMockMvc.perform(put("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isOk());

        // Validate the Discount in the database
        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeUpdate);
        Discount testDiscount = discounts.get(discounts.size() - 1);
        assertThat(testDiscount.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDiscount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDiscount.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testDiscount.getUnits()).isEqualTo(UPDATED_UNITS);
        assertThat(testDiscount.getActiveFrom()).isEqualTo(UPDATED_ACTIVE_FROM);
        assertThat(testDiscount.getActiveTo()).isEqualTo(UPDATED_ACTIVE_TO);
    }

    @Test
    @Transactional
    public void deleteDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

		int databaseSizeBeforeDelete = discountRepository.findAll().size();

        // Get the discount
        restDiscountMockMvc.perform(delete("/api/discounts/{id}", discount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
