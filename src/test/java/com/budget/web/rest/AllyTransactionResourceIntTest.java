package com.budget.web.rest;

import com.budget.BudgetApp;

import com.budget.domain.AllyTransaction;
import com.budget.repository.AllyTransactionRepository;
import com.budget.service.AllyTransactionService;
import com.budget.repository.search.AllyTransactionSearchRepository;
import com.budget.service.dto.AllyTransactionDTO;
import com.budget.service.mapper.AllyTransactionMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.budget.domain.enumeration.AllyTransactionType;
import com.budget.domain.enumeration.AccountType;
/**
 * Test class for the AllyTransactionResource REST controller.
 *
 * @see AllyTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class AllyTransactionResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATE);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final AllyTransactionType DEFAULT_TRANSACTION_TYPE = AllyTransactionType.DEPOSIT;
    private static final AllyTransactionType UPDATED_TRANSACTION_TYPE = AllyTransactionType.WITHDRAWAL;

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_BUDGETED = false;
    private static final Boolean UPDATED_BUDGETED = true;

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.CHECKING;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.SAVINGS;

    @Inject
    private AllyTransactionRepository allyTransactionRepository;

    @Inject
    private AllyTransactionMapper allyTransactionMapper;

    @Inject
    private AllyTransactionService allyTransactionService;

    @Inject
    private AllyTransactionSearchRepository allyTransactionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAllyTransactionMockMvc;

    private AllyTransaction allyTransaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AllyTransactionResource allyTransactionResource = new AllyTransactionResource();
        ReflectionTestUtils.setField(allyTransactionResource, "allyTransactionService", allyTransactionService);
        this.restAllyTransactionMockMvc = MockMvcBuilders.standaloneSetup(allyTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllyTransaction createEntity(EntityManager em) {
        AllyTransaction allyTransaction = new AllyTransaction()
                .date(DEFAULT_DATE)
                .amount(DEFAULT_AMOUNT)
                .transactionType(DEFAULT_TRANSACTION_TYPE)
                .description(DEFAULT_DESCRIPTION)
                .budgeted(DEFAULT_BUDGETED)
                .accountType(DEFAULT_ACCOUNT_TYPE);
        return allyTransaction;
    }

    @Before
    public void initTest() {
        allyTransactionSearchRepository.deleteAll();
        allyTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createAllyTransaction() throws Exception {
        int databaseSizeBeforeCreate = allyTransactionRepository.findAll().size();

        // Create the AllyTransaction
        AllyTransactionDTO allyTransactionDTO = allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction);

        restAllyTransactionMockMvc.perform(post("/api/ally-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allyTransactionDTO)))
                .andExpect(status().isCreated());

        // Validate the AllyTransaction in the database
        List<AllyTransaction> allyTransactions = allyTransactionRepository.findAll();
        assertThat(allyTransactions).hasSize(databaseSizeBeforeCreate + 1);
        AllyTransaction testAllyTransaction = allyTransactions.get(allyTransactions.size() - 1);
        assertThat(testAllyTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAllyTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAllyTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testAllyTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAllyTransaction.isBudgeted()).isEqualTo(DEFAULT_BUDGETED);
        assertThat(testAllyTransaction.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);

        // Validate the AllyTransaction in ElasticSearch
        AllyTransaction allyTransactionEs = allyTransactionSearchRepository.findOne(testAllyTransaction.getId());
        assertThat(allyTransactionEs).isEqualToComparingFieldByField(testAllyTransaction);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = allyTransactionRepository.findAll().size();
        // set the field null
        allyTransaction.setDate(null);

        // Create the AllyTransaction, which fails.
        AllyTransactionDTO allyTransactionDTO = allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction);

        restAllyTransactionMockMvc.perform(post("/api/ally-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allyTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<AllyTransaction> allyTransactions = allyTransactionRepository.findAll();
        assertThat(allyTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = allyTransactionRepository.findAll().size();
        // set the field null
        allyTransaction.setAmount(null);

        // Create the AllyTransaction, which fails.
        AllyTransactionDTO allyTransactionDTO = allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction);

        restAllyTransactionMockMvc.perform(post("/api/ally-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allyTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<AllyTransaction> allyTransactions = allyTransactionRepository.findAll();
        assertThat(allyTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = allyTransactionRepository.findAll().size();
        // set the field null
        allyTransaction.setTransactionType(null);

        // Create the AllyTransaction, which fails.
        AllyTransactionDTO allyTransactionDTO = allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction);

        restAllyTransactionMockMvc.perform(post("/api/ally-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allyTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<AllyTransaction> allyTransactions = allyTransactionRepository.findAll();
        assertThat(allyTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = allyTransactionRepository.findAll().size();
        // set the field null
        allyTransaction.setDescription(null);

        // Create the AllyTransaction, which fails.
        AllyTransactionDTO allyTransactionDTO = allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction);

        restAllyTransactionMockMvc.perform(post("/api/ally-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allyTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<AllyTransaction> allyTransactions = allyTransactionRepository.findAll();
        assertThat(allyTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = allyTransactionRepository.findAll().size();
        // set the field null
        allyTransaction.setAccountType(null);

        // Create the AllyTransaction, which fails.
        AllyTransactionDTO allyTransactionDTO = allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction);

        restAllyTransactionMockMvc.perform(post("/api/ally-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allyTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<AllyTransaction> allyTransactions = allyTransactionRepository.findAll();
        assertThat(allyTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAllyTransactions() throws Exception {
        // Initialize the database
        allyTransactionRepository.saveAndFlush(allyTransaction);

        // Get all the allyTransactions
        restAllyTransactionMockMvc.perform(get("/api/ally-transactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(allyTransaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].budgeted").value(hasItem(DEFAULT_BUDGETED.booleanValue())))
                .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getAllyTransaction() throws Exception {
        // Initialize the database
        allyTransactionRepository.saveAndFlush(allyTransaction);

        // Get the allyTransaction
        restAllyTransactionMockMvc.perform(get("/api/ally-transactions/{id}", allyTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(allyTransaction.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.budgeted").value(DEFAULT_BUDGETED.booleanValue()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAllyTransaction() throws Exception {
        // Get the allyTransaction
        restAllyTransactionMockMvc.perform(get("/api/ally-transactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAllyTransaction() throws Exception {
        // Initialize the database
        allyTransactionRepository.saveAndFlush(allyTransaction);
        allyTransactionSearchRepository.save(allyTransaction);
        int databaseSizeBeforeUpdate = allyTransactionRepository.findAll().size();

        // Update the allyTransaction
        AllyTransaction updatedAllyTransaction = allyTransactionRepository.findOne(allyTransaction.getId());
        updatedAllyTransaction
                .date(UPDATED_DATE)
                .amount(UPDATED_AMOUNT)
                .transactionType(UPDATED_TRANSACTION_TYPE)
                .description(UPDATED_DESCRIPTION)
                .budgeted(UPDATED_BUDGETED)
                .accountType(UPDATED_ACCOUNT_TYPE);
        AllyTransactionDTO allyTransactionDTO = allyTransactionMapper.allyTransactionToAllyTransactionDTO(updatedAllyTransaction);

        restAllyTransactionMockMvc.perform(put("/api/ally-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allyTransactionDTO)))
                .andExpect(status().isOk());

        // Validate the AllyTransaction in the database
        List<AllyTransaction> allyTransactions = allyTransactionRepository.findAll();
        assertThat(allyTransactions).hasSize(databaseSizeBeforeUpdate);
        AllyTransaction testAllyTransaction = allyTransactions.get(allyTransactions.size() - 1);
        assertThat(testAllyTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAllyTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAllyTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testAllyTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAllyTransaction.isBudgeted()).isEqualTo(UPDATED_BUDGETED);
        assertThat(testAllyTransaction.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);

        // Validate the AllyTransaction in ElasticSearch
        AllyTransaction allyTransactionEs = allyTransactionSearchRepository.findOne(testAllyTransaction.getId());
        assertThat(allyTransactionEs).isEqualToComparingFieldByField(testAllyTransaction);
    }

    @Test
    @Transactional
    public void deleteAllyTransaction() throws Exception {
        // Initialize the database
        allyTransactionRepository.saveAndFlush(allyTransaction);
        allyTransactionSearchRepository.save(allyTransaction);
        int databaseSizeBeforeDelete = allyTransactionRepository.findAll().size();

        // Get the allyTransaction
        restAllyTransactionMockMvc.perform(delete("/api/ally-transactions/{id}", allyTransaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean allyTransactionExistsInEs = allyTransactionSearchRepository.exists(allyTransaction.getId());
        assertThat(allyTransactionExistsInEs).isFalse();

        // Validate the database is empty
        List<AllyTransaction> allyTransactions = allyTransactionRepository.findAll();
        assertThat(allyTransactions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAllyTransaction() throws Exception {
        // Initialize the database
        allyTransactionRepository.saveAndFlush(allyTransaction);
        allyTransactionSearchRepository.save(allyTransaction);

        // Search the allyTransaction
        restAllyTransactionMockMvc.perform(get("/api/_search/ally-transactions?query=id:" + allyTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allyTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].budgeted").value(hasItem(DEFAULT_BUDGETED.booleanValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())));
    }
}
