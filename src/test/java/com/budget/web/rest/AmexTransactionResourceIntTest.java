package com.budget.web.rest;

import com.budget.BudgetApp;

import com.budget.domain.AmexTransaction;
import com.budget.repository.AmexTransactionRepository;
import com.budget.service.AmexTransactionService;
import com.budget.repository.search.AmexTransactionSearchRepository;
import com.budget.service.dto.AmexTransactionDTO;
import com.budget.service.mapper.AmexTransactionMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AmexTransactionResource REST controller.
 *
 * @see AmexTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class AmexTransactionResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_PERSON = "AAAAA";
    private static final String UPDATED_PERSON = "BBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Long DEFAULT_REFERENCE_ID = 1L;
    private static final Long UPDATED_REFERENCE_ID = 2L;

    private static final Boolean DEFAULT_BUDGETED = false;
    private static final Boolean UPDATED_BUDGETED = true;

    @Inject
    private AmexTransactionRepository amexTransactionRepository;

    @Inject
    private AmexTransactionMapper amexTransactionMapper;

    @Inject
    private AmexTransactionService amexTransactionService;

    @Inject
    private AmexTransactionSearchRepository amexTransactionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAmexTransactionMockMvc;

    private AmexTransaction amexTransaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AmexTransactionResource amexTransactionResource = new AmexTransactionResource();
        ReflectionTestUtils.setField(amexTransactionResource, "amexTransactionService", amexTransactionService);
        this.restAmexTransactionMockMvc = MockMvcBuilders.standaloneSetup(amexTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmexTransaction createEntity(EntityManager em) {
        AmexTransaction amexTransaction = new AmexTransaction()
                .date(DEFAULT_DATE)
                .description(DEFAULT_DESCRIPTION)
                .person(DEFAULT_PERSON)
                .amount(DEFAULT_AMOUNT)
                .referenceId(DEFAULT_REFERENCE_ID)
                .budgeted(DEFAULT_BUDGETED);
        return amexTransaction;
    }

    @Before
    public void initTest() {
        amexTransactionSearchRepository.deleteAll();
        amexTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createAmexTransaction() throws Exception {
        int databaseSizeBeforeCreate = amexTransactionRepository.findAll().size();

        // Create the AmexTransaction
        AmexTransactionDTO amexTransactionDTO = amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction);

        restAmexTransactionMockMvc.perform(post("/api/amex-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(amexTransactionDTO)))
                .andExpect(status().isCreated());

        // Validate the AmexTransaction in the database
        List<AmexTransaction> amexTransactions = amexTransactionRepository.findAll();
        assertThat(amexTransactions).hasSize(databaseSizeBeforeCreate + 1);
        AmexTransaction testAmexTransaction = amexTransactions.get(amexTransactions.size() - 1);
        assertThat(testAmexTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAmexTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAmexTransaction.getPerson()).isEqualTo(DEFAULT_PERSON);
        assertThat(testAmexTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAmexTransaction.getReferenceId()).isEqualTo(DEFAULT_REFERENCE_ID);
        assertThat(testAmexTransaction.isBudgeted()).isEqualTo(DEFAULT_BUDGETED);

        // Validate the AmexTransaction in ElasticSearch
        AmexTransaction amexTransactionEs = amexTransactionSearchRepository.findOne(testAmexTransaction.getId());
        assertThat(amexTransactionEs).isEqualToComparingFieldByField(testAmexTransaction);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = amexTransactionRepository.findAll().size();
        // set the field null
        amexTransaction.setDate(null);

        // Create the AmexTransaction, which fails.
        AmexTransactionDTO amexTransactionDTO = amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction);

        restAmexTransactionMockMvc.perform(post("/api/amex-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(amexTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<AmexTransaction> amexTransactions = amexTransactionRepository.findAll();
        assertThat(amexTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = amexTransactionRepository.findAll().size();
        // set the field null
        amexTransaction.setDescription(null);

        // Create the AmexTransaction, which fails.
        AmexTransactionDTO amexTransactionDTO = amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction);

        restAmexTransactionMockMvc.perform(post("/api/amex-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(amexTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<AmexTransaction> amexTransactions = amexTransactionRepository.findAll();
        assertThat(amexTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = amexTransactionRepository.findAll().size();
        // set the field null
        amexTransaction.setAmount(null);

        // Create the AmexTransaction, which fails.
        AmexTransactionDTO amexTransactionDTO = amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction);

        restAmexTransactionMockMvc.perform(post("/api/amex-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(amexTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<AmexTransaction> amexTransactions = amexTransactionRepository.findAll();
        assertThat(amexTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAmexTransactions() throws Exception {
        // Initialize the database
        amexTransactionRepository.saveAndFlush(amexTransaction);

        // Get all the amexTransactions
        restAmexTransactionMockMvc.perform(get("/api/amex-transactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(amexTransaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].person").value(hasItem(DEFAULT_PERSON.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].referenceId").value(hasItem(DEFAULT_REFERENCE_ID.intValue())))
                .andExpect(jsonPath("$.[*].budgeted").value(hasItem(DEFAULT_BUDGETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getAmexTransaction() throws Exception {
        // Initialize the database
        amexTransactionRepository.saveAndFlush(amexTransaction);

        // Get the amexTransaction
        restAmexTransactionMockMvc.perform(get("/api/amex-transactions/{id}", amexTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(amexTransaction.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.person").value(DEFAULT_PERSON.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.referenceId").value(DEFAULT_REFERENCE_ID.intValue()))
            .andExpect(jsonPath("$.budgeted").value(DEFAULT_BUDGETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAmexTransaction() throws Exception {
        // Get the amexTransaction
        restAmexTransactionMockMvc.perform(get("/api/amex-transactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAmexTransaction() throws Exception {
        // Initialize the database
        amexTransactionRepository.saveAndFlush(amexTransaction);
        amexTransactionSearchRepository.save(amexTransaction);
        int databaseSizeBeforeUpdate = amexTransactionRepository.findAll().size();

        // Update the amexTransaction
        AmexTransaction updatedAmexTransaction = amexTransactionRepository.findOne(amexTransaction.getId());
        updatedAmexTransaction
                .date(UPDATED_DATE)
                .description(UPDATED_DESCRIPTION)
                .person(UPDATED_PERSON)
                .amount(UPDATED_AMOUNT)
                .referenceId(UPDATED_REFERENCE_ID)
                .budgeted(UPDATED_BUDGETED);
        AmexTransactionDTO amexTransactionDTO = amexTransactionMapper.amexTransactionToAmexTransactionDTO(updatedAmexTransaction);

        restAmexTransactionMockMvc.perform(put("/api/amex-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(amexTransactionDTO)))
                .andExpect(status().isOk());

        // Validate the AmexTransaction in the database
        List<AmexTransaction> amexTransactions = amexTransactionRepository.findAll();
        assertThat(amexTransactions).hasSize(databaseSizeBeforeUpdate);
        AmexTransaction testAmexTransaction = amexTransactions.get(amexTransactions.size() - 1);
        assertThat(testAmexTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAmexTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAmexTransaction.getPerson()).isEqualTo(UPDATED_PERSON);
        assertThat(testAmexTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAmexTransaction.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testAmexTransaction.isBudgeted()).isEqualTo(UPDATED_BUDGETED);

        // Validate the AmexTransaction in ElasticSearch
        AmexTransaction amexTransactionEs = amexTransactionSearchRepository.findOne(testAmexTransaction.getId());
        assertThat(amexTransactionEs).isEqualToComparingFieldByField(testAmexTransaction);
    }

    @Test
    @Transactional
    public void deleteAmexTransaction() throws Exception {
        // Initialize the database
        amexTransactionRepository.saveAndFlush(amexTransaction);
        amexTransactionSearchRepository.save(amexTransaction);
        int databaseSizeBeforeDelete = amexTransactionRepository.findAll().size();

        // Get the amexTransaction
        restAmexTransactionMockMvc.perform(delete("/api/amex-transactions/{id}", amexTransaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean amexTransactionExistsInEs = amexTransactionSearchRepository.exists(amexTransaction.getId());
        assertThat(amexTransactionExistsInEs).isFalse();

        // Validate the database is empty
        List<AmexTransaction> amexTransactions = amexTransactionRepository.findAll();
        assertThat(amexTransactions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAmexTransaction() throws Exception {
        // Initialize the database
        amexTransactionRepository.saveAndFlush(amexTransaction);
        amexTransactionSearchRepository.save(amexTransaction);

        // Search the amexTransaction
        restAmexTransactionMockMvc.perform(get("/api/_search/amex-transactions?query=id:" + amexTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amexTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].person").value(hasItem(DEFAULT_PERSON.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].referenceId").value(hasItem(DEFAULT_REFERENCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].budgeted").value(hasItem(DEFAULT_BUDGETED.booleanValue())));
    }
}
