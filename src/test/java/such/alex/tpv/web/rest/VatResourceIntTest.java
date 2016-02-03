package such.alex.tpv.web.rest;

import com.google.common.eventbus.AllowConcurrentEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Propagation;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;
import such.alex.tpv.Application;
import such.alex.tpv.domain.Category;
import such.alex.tpv.domain.Vat;
import such.alex.tpv.repository.CategoryRepository;
import such.alex.tpv.repository.VatRepository;
import such.alex.tpv.service.CategoryService;
import such.alex.tpv.service.VatService;

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
import such.alex.tpv.service.util.Constants;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the VatResource REST controller.
 *
 * @see VatResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VatResourceIntTest {

    private static final String DEFAULT_CODE = "AAAA";
    private static final String UPDATED_CODE = "BBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Float DEFAULT_VALUE = 1F;
    private static final Float UPDATED_VALUE = 2F;

    @Inject
    private VatRepository vatRepository;

    @Inject
    private VatService vatService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVatMockMvc;

    private Vat vat;

    private Category category;

    @Inject
    EventBus eventBus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VatResource vatResource = new VatResource();
        ReflectionTestUtils.setField(vatResource, "vatService", vatService);
        this.restVatMockMvc = MockMvcBuilders.standaloneSetup(vatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        vat = new Vat();
        vat.setCode(DEFAULT_CODE);
        vat.setDescription(DEFAULT_DESCRIPTION);
        vat.setValue(DEFAULT_VALUE);

        category = new Category();
        category.setName("CAT1");
        category.setDescription("CAT1");
    }

    @Test
    @Transactional
    public void createVat() throws Exception {
        int databaseSizeBeforeCreate = vatRepository.findAll().size();

        // Create the Vat

        restVatMockMvc.perform(post("/api/vats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vat)))
            .andExpect(status().isCreated());

        // Validate the Vat in the database
        List<Vat> vats = vatRepository.findAll();
        assertThat(vats).hasSize(databaseSizeBeforeCreate + 1);
        Vat testVat = vats.get(vats.size() - 1);
        assertThat(testVat.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVat.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vatRepository.findAll().size();
        // set the field null
        vat.setCode(null);

        // Create the Vat, which fails.

        restVatMockMvc.perform(post("/api/vats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vat)))
            .andExpect(status().isBadRequest());

        List<Vat> vats = vatRepository.findAll();
        assertThat(vats).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = vatRepository.findAll().size();
        // set the field null
        vat.setDescription(null);

        // Create the Vat, which fails.

        restVatMockMvc.perform(post("/api/vats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vat)))
            .andExpect(status().isBadRequest());

        List<Vat> vats = vatRepository.findAll();
        assertThat(vats).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = vatRepository.findAll().size();
        // set the field null
        vat.setValue(null);

        // Create the Vat, which fails.

        restVatMockMvc.perform(post("/api/vats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vat)))
            .andExpect(status().isBadRequest());

        List<Vat> vats = vatRepository.findAll();
        assertThat(vats).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVats() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        // Get all the vats
        restVatMockMvc.perform(get("/api/vats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vat.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    public void getVat() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        // Get the vat
        restVatMockMvc.perform(get("/api/vats/{id}", vat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(vat.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVat() throws Exception {
        // Get the vat
        restVatMockMvc.perform(get("/api/vats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVat() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        int databaseSizeBeforeUpdate = vatRepository.findAll().size();

        // Update the vat
        vat.setCode(UPDATED_CODE);
        vat.setDescription(UPDATED_DESCRIPTION);
        vat.setValue(UPDATED_VALUE);

        restVatMockMvc.perform(put("/api/vats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vat)))
            .andExpect(status().isOk());

        // Validate the Vat in the database
        List<Vat> vats = vatRepository.findAll();
        assertThat(vats).hasSize(databaseSizeBeforeUpdate);
        Vat testVat = vats.get(vats.size() - 1);
        assertThat(testVat.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVat.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteVat() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        int databaseSizeBeforeDelete = vatRepository.findAll().size();

        // Get the vat
        restVatMockMvc.perform(delete("/api/vats/{id}", vat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vat> vats = vatRepository.findAll();
        assertThat(vats).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void checkThatClonesOnUpdate() {
        vatRepository.saveAndFlush(vat);

        vat.setDescription("Changed Description");

        Vat newVat = vatRepository.saveWithHistoric(vat);

        assertThat(vat.getId()).isNotEqualTo(newVat.getId());
    }

    @Test
    @AllowConcurrentEvents
    @Transactional
    public void checkThatClonesOnUpdateAndCategoryChangesItsVat() {
        final CountDownLatch latch = new CountDownLatch(1);

        vatRepository.saveAndFlush(vat);
        category.setVat(vat);

        categoryRepository.saveAndFlush(category);

        vat.setDescription("Changed Description");

        eventBus.on(Selectors.$(Constants.CATEGORIES_UPDATED), new Consumer<Event<List<Category>>>() {
            @Override
            public void accept(Event<List<Category>>event) {
                latch.countDown();
            }
        });

        Vat newVat = vatService.saveWithHistoric(vat);

        assertThat(vat.getId()).isNotEqualTo(newVat.getId());


        try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //final Category categoryFromDb = categoryService.findOne(category.getId());
        //assertThat(newVat.getId()).isEqualTo(categoryFromDb.getVat().getId());
    }
}
