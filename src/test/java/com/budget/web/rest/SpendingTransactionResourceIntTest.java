package com.budget.web.rest;

import com.budget.BudgetApp;

import com.budget.domain.SpendingTransaction;
import com.budget.repository.SpendingTransactionRepository;
import com.budget.service.SpendingTransactionService;
import com.budget.repository.search.SpendingTransactionSearchRepository;
import com.budget.service.dto.SpendingTransactionDTO;
import com.budget.service.mapper.SpendingTransactionMapper;

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
 * Test class for the SpendingTransactionResource REST controller.
 *
 * @see SpendingTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class SpendingTransactionResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private SpendingTransactionRepository spendingTransactionRepository;

    @Inject
    private SpendingTransactionMapper spendingTransactionMapper;

    @Inject
    private SpendingTransactionService spendingTransactionService;

    @Inject
    private SpendingTransactionSearchRepository spendingTransactionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSpendingTransactionMockMvc;

    private SpendingTransaction spendingTransaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpendingTransactionResource spendingTransactionResource = new SpendingTransactionResource();
        ReflectionTestUtils.setField(spendingTransactionResource, "spendingTransactionService", spendingTransactionService);
        this.restSpendingTransactionMockMvc = MockMvcBuilders.standaloneSetup(spendingTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpendingTransaction createEntity(EntityManager em) {
        SpendingTransaction spendingTransaction = new SpendingTransaction()
                .date(DEFAULT_DATE)
                .amount(DEFAULT_AMOUNT);
        return spendingTransaction;
    }

    @Before
    public void initTest() {
        spendingTransactionSearchRepository.deleteAll();
        spendingTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpendingTransaction() throws Exception {
        int databaseSizeBeforeCreate = spendingTransactionRepository.findAll().size();

        // Create the SpendingTransaction
        SpendingTransactionDTO spendingTransactionDTO = spendingTransactionMapper.spendingTransactionToSpendingTransactionDTO(spendingTransaction);

        restSpendingTransactionMockMvc.perform(post("/api/spending-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spendingTransactionDTO)))
                .andExpect(status().isCreated());

        // Validate the SpendingTransaction in the database
        List<SpendingTransaction> spendingTransactions = spendingTransactionRepository.findAll();
        assertThat(spendingTransactions).hasSize(databaseSizeBeforeCreate + 1);
        SpendingTransaction testSpendingTransaction = spendingTransactions.get(spendingTransactions.size() - 1);
        assertThat(testSpendingTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSpendingTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the SpendingTransaction in ElasticSearch
        SpendingTransaction spendingTransactionEs = spendingTransactionSearchRepository.findOne(testSpendingTransaction.getId());
        assertThat(spendingTransactionEs).isEqualToComparingFieldByField(testSpendingTransaction);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = spendingTransactionRepository.findAll().size();
        // set the field null
        spendingTransaction.setDate(null);

        // Create the SpendingTransaction, which fails.
        SpendingTransactionDTO spendingTransactionDTO = spendingTransactionMapper.spendingTransactionToSpendingTransactionDTO(spendingTransaction);

        restSpendingTransactionMockMvc.perform(post("/api/spending-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spendingTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<SpendingTransaction> spendingTransactions = spendingTransactionRepository.findAll();
        assertThat(spendingTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = spendingTransactionRepository.findAll().size();
        // set the field null
        spendingTransaction.setAmount(null);

        // Create the SpendingTransaction, which fails.
        SpendingTransactionDTO spendingTransactionDTO = spendingTransactionMapper.spendingTransactionToSpendingTransactionDTO(spendingTransaction);

        restSpendingTransactionMockMvc.perform(post("/api/spending-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spendingTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<SpendingTransaction> spendingTransactions = spendingTransactionRepository.findAll();
        assertThat(spendingTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpendingTransactions() throws Exception {
        // Initialize the database
        spendingTransactionRepository.saveAndFlush(spendingTransaction);

        // Get all the spendingTransactions
        restSpendingTransactionMockMvc.perform(get("/api/spending-transactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(spendingTransaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getSpendingTransaction() throws Exception {
        // Initialize the database
        spendingTransactionRepository.saveAndFlush(spendingTransaction);

        // Get the spendingTransaction
        restSpendingTransactionMockMvc.perform(get("/api/spending-transactions/{id}", spendingTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spendingTransaction.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSpendingTransaction() throws Exception {
        // Get the spendingTransaction
        restSpendingTransactionMockMvc.perform(get("/api/spending-transactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpendingTransaction() throws Exception {
        // Initialize the database
        spendingTransactionRepository.saveAndFlush(spendingTransaction);
        spendingTransactionSearchRepository.save(spendingTransaction);
        int databaseSizeBeforeUpdate = spendingTransactionRepository.findAll().size();

        // Update the spendingTransaction
        SpendingTransaction updatedSpendingTransaction = spendingTransactionRepository.findOne(spendingTransaction.getId());
        updatedSpendingTransaction
                .date(UPDATED_DATE)
                .amount(UPDATED_AMOUNT);
        SpendingTransactionDTO spendingTransactionDTO = spendingTransactionMapper.spendingTransactionToSpendingTransactionDTO(updatedSpendingTransaction);

        restSpendingTransactionMockMvc.perform(put("/api/spending-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spendingTransactionDTO)))
                .andExpect(status().isOk());

        // Validate the SpendingTransaction in the database
        List<SpendingTransaction> spendingTransactions = spendingTransactionRepository.findAll();
        assertThat(spendingTransactions).hasSize(databaseSizeBeforeUpdate);
        SpendingTransaction testSpendingTransaction = spendingTransactions.get(spendingTransactions.size() - 1);
        assertThat(testSpendingTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSpendingTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the SpendingTransaction in ElasticSearch
        SpendingTransaction spendingTransactionEs = spendingTransactionSearchRepository.findOne(testSpendingTransaction.getId());
        assertThat(spendingTransactionEs).isEqualToComparingFieldByField(testSpendingTransaction);
    }

    @Test
    @Transactional
    public void deleteSpendingTransaction() throws Exception {
        // Initialize the database
        spendingTransactionRepository.saveAndFlush(spendingTransaction);
        spendingTransactionSearchRepository.save(spendingTransaction);
        int databaseSizeBeforeDelete = spendingTransactionRepository.findAll().size();

        // Get the spendingTransaction
        restSpendingTransactionMockMvc.perform(delete("/api/spending-transactions/{id}", spendingTransaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean spendingTransactionExistsInEs = spendingTransactionSearchRepository.exists(spendingTransaction.getId());
        assertThat(spendingTransactionExistsInEs).isFalse();

        // Validate the database is empty
        List<SpendingTransaction> spendingTransactions = spendingTransactionRepository.findAll();
        assertThat(spendingTransactions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSpendingTransaction() throws Exception {
        // Initialize the database
        spendingTransactionRepository.saveAndFlush(spendingTransaction);
        spendingTransactionSearchRepository.save(spendingTransaction);

        // Search the spendingTransaction
        restSpendingTransactionMockMvc.perform(get("/api/_search/spending-transactions?query=id:" + spendingTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spendingTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }
}
