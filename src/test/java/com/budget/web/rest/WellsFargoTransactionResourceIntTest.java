package com.budget.web.rest;

import com.budget.BudgetApp;

import com.budget.domain.WellsFargoTransaction;
import com.budget.repository.WellsFargoTransactionRepository;
import com.budget.service.WellsFargoTransactionService;
import com.budget.repository.search.WellsFargoTransactionSearchRepository;

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

import com.budget.domain.enumeration.AccountType;
/**
 * Test class for the WellsFargoTransactionResource REST controller.
 *
 * @see WellsFargoTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class WellsFargoTransactionResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_BUDGETED = false;
    private static final Boolean UPDATED_BUDGETED = true;

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.CHECKING;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.SAVINGS;

    @Inject
    private WellsFargoTransactionRepository wellsFargoTransactionRepository;

    @Inject
    private WellsFargoTransactionService wellsFargoTransactionService;

    @Inject
    private WellsFargoTransactionSearchRepository wellsFargoTransactionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWellsFargoTransactionMockMvc;

    private WellsFargoTransaction wellsFargoTransaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WellsFargoTransactionResource wellsFargoTransactionResource = new WellsFargoTransactionResource();
        ReflectionTestUtils.setField(wellsFargoTransactionResource, "wellsFargoTransactionService", wellsFargoTransactionService);
        this.restWellsFargoTransactionMockMvc = MockMvcBuilders.standaloneSetup(wellsFargoTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WellsFargoTransaction createEntity(EntityManager em) {
        WellsFargoTransaction wellsFargoTransaction = new WellsFargoTransaction()
                .date(DEFAULT_DATE)
                .amount(DEFAULT_AMOUNT)
                .description(DEFAULT_DESCRIPTION)
                .budgeted(DEFAULT_BUDGETED)
                .accountType(DEFAULT_ACCOUNT_TYPE);
        return wellsFargoTransaction;
    }

    @Before
    public void initTest() {
        wellsFargoTransactionSearchRepository.deleteAll();
        wellsFargoTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createWellsFargoTransaction() throws Exception {
        int databaseSizeBeforeCreate = wellsFargoTransactionRepository.findAll().size();

        // Create the WellsFargoTransaction

        restWellsFargoTransactionMockMvc.perform(post("/api/wells-fargo-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wellsFargoTransaction)))
                .andExpect(status().isCreated());

        // Validate the WellsFargoTransaction in the database
        List<WellsFargoTransaction> wellsFargoTransactions = wellsFargoTransactionRepository.findAll();
        assertThat(wellsFargoTransactions).hasSize(databaseSizeBeforeCreate + 1);
        WellsFargoTransaction testWellsFargoTransaction = wellsFargoTransactions.get(wellsFargoTransactions.size() - 1);
        assertThat(testWellsFargoTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testWellsFargoTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testWellsFargoTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWellsFargoTransaction.isBudgeted()).isEqualTo(DEFAULT_BUDGETED);
        assertThat(testWellsFargoTransaction.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);

        // Validate the WellsFargoTransaction in ElasticSearch
        WellsFargoTransaction wellsFargoTransactionEs = wellsFargoTransactionSearchRepository.findOne(testWellsFargoTransaction.getId());
        assertThat(wellsFargoTransactionEs).isEqualToComparingFieldByField(testWellsFargoTransaction);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = wellsFargoTransactionRepository.findAll().size();
        // set the field null
        wellsFargoTransaction.setDate(null);

        // Create the WellsFargoTransaction, which fails.

        restWellsFargoTransactionMockMvc.perform(post("/api/wells-fargo-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wellsFargoTransaction)))
                .andExpect(status().isBadRequest());

        List<WellsFargoTransaction> wellsFargoTransactions = wellsFargoTransactionRepository.findAll();
        assertThat(wellsFargoTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = wellsFargoTransactionRepository.findAll().size();
        // set the field null
        wellsFargoTransaction.setAmount(null);

        // Create the WellsFargoTransaction, which fails.

        restWellsFargoTransactionMockMvc.perform(post("/api/wells-fargo-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wellsFargoTransaction)))
                .andExpect(status().isBadRequest());

        List<WellsFargoTransaction> wellsFargoTransactions = wellsFargoTransactionRepository.findAll();
        assertThat(wellsFargoTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = wellsFargoTransactionRepository.findAll().size();
        // set the field null
        wellsFargoTransaction.setDescription(null);

        // Create the WellsFargoTransaction, which fails.

        restWellsFargoTransactionMockMvc.perform(post("/api/wells-fargo-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wellsFargoTransaction)))
                .andExpect(status().isBadRequest());

        List<WellsFargoTransaction> wellsFargoTransactions = wellsFargoTransactionRepository.findAll();
        assertThat(wellsFargoTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWellsFargoTransactions() throws Exception {
        // Initialize the database
        wellsFargoTransactionRepository.saveAndFlush(wellsFargoTransaction);

        // Get all the wellsFargoTransactions
        restWellsFargoTransactionMockMvc.perform(get("/api/wells-fargo-transactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(wellsFargoTransaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].budgeted").value(hasItem(DEFAULT_BUDGETED.booleanValue())))
                .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getWellsFargoTransaction() throws Exception {
        // Initialize the database
        wellsFargoTransactionRepository.saveAndFlush(wellsFargoTransaction);

        // Get the wellsFargoTransaction
        restWellsFargoTransactionMockMvc.perform(get("/api/wells-fargo-transactions/{id}", wellsFargoTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wellsFargoTransaction.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.budgeted").value(DEFAULT_BUDGETED.booleanValue()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWellsFargoTransaction() throws Exception {
        // Get the wellsFargoTransaction
        restWellsFargoTransactionMockMvc.perform(get("/api/wells-fargo-transactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWellsFargoTransaction() throws Exception {
        // Initialize the database
        wellsFargoTransactionService.save(wellsFargoTransaction);

        int databaseSizeBeforeUpdate = wellsFargoTransactionRepository.findAll().size();

        // Update the wellsFargoTransaction
        WellsFargoTransaction updatedWellsFargoTransaction = wellsFargoTransactionRepository.findOne(wellsFargoTransaction.getId());
        updatedWellsFargoTransaction
                .date(UPDATED_DATE)
                .amount(UPDATED_AMOUNT)
                .description(UPDATED_DESCRIPTION)
                .budgeted(UPDATED_BUDGETED)
                .accountType(UPDATED_ACCOUNT_TYPE);

        restWellsFargoTransactionMockMvc.perform(put("/api/wells-fargo-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWellsFargoTransaction)))
                .andExpect(status().isOk());

        // Validate the WellsFargoTransaction in the database
        List<WellsFargoTransaction> wellsFargoTransactions = wellsFargoTransactionRepository.findAll();
        assertThat(wellsFargoTransactions).hasSize(databaseSizeBeforeUpdate);
        WellsFargoTransaction testWellsFargoTransaction = wellsFargoTransactions.get(wellsFargoTransactions.size() - 1);
        assertThat(testWellsFargoTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testWellsFargoTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testWellsFargoTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWellsFargoTransaction.isBudgeted()).isEqualTo(UPDATED_BUDGETED);
        assertThat(testWellsFargoTransaction.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);

        // Validate the WellsFargoTransaction in ElasticSearch
        WellsFargoTransaction wellsFargoTransactionEs = wellsFargoTransactionSearchRepository.findOne(testWellsFargoTransaction.getId());
        assertThat(wellsFargoTransactionEs).isEqualToComparingFieldByField(testWellsFargoTransaction);
    }

    @Test
    @Transactional
    public void deleteWellsFargoTransaction() throws Exception {
        // Initialize the database
        wellsFargoTransactionService.save(wellsFargoTransaction);

        int databaseSizeBeforeDelete = wellsFargoTransactionRepository.findAll().size();

        // Get the wellsFargoTransaction
        restWellsFargoTransactionMockMvc.perform(delete("/api/wells-fargo-transactions/{id}", wellsFargoTransaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean wellsFargoTransactionExistsInEs = wellsFargoTransactionSearchRepository.exists(wellsFargoTransaction.getId());
        assertThat(wellsFargoTransactionExistsInEs).isFalse();

        // Validate the database is empty
        List<WellsFargoTransaction> wellsFargoTransactions = wellsFargoTransactionRepository.findAll();
        assertThat(wellsFargoTransactions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWellsFargoTransaction() throws Exception {
        // Initialize the database
        wellsFargoTransactionService.save(wellsFargoTransaction);

        // Search the wellsFargoTransaction
        restWellsFargoTransactionMockMvc.perform(get("/api/_search/wells-fargo-transactions?query=id:" + wellsFargoTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wellsFargoTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].budgeted").value(hasItem(DEFAULT_BUDGETED.booleanValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())));
    }
}
