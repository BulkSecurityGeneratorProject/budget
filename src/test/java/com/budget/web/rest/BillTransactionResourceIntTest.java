package com.budget.web.rest;

import com.budget.BudgetApp;

import com.budget.domain.BillTransaction;
import com.budget.repository.BillTransactionRepository;
import com.budget.service.BillTransactionService;
import com.budget.repository.search.BillTransactionSearchRepository;
import com.budget.service.dto.BillTransactionDTO;
import com.budget.service.mapper.BillTransactionMapper;

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
 * Test class for the BillTransactionResource REST controller.
 *
 * @see BillTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class BillTransactionResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Integer DEFAULT_DAY_OUT = 1;
    private static final Integer UPDATED_DAY_OUT = 2;

    private static final String DEFAULT_FROM_ACCOUNT = "AAAAA";
    private static final String UPDATED_FROM_ACCOUNT = "BBBBB";

    private static final Boolean DEFAULT_AUTOMATIC = false;
    private static final Boolean UPDATED_AUTOMATIC = true;

    @Inject
    private BillTransactionRepository billTransactionRepository;

    @Inject
    private BillTransactionMapper billTransactionMapper;

    @Inject
    private BillTransactionService billTransactionService;

    @Inject
    private BillTransactionSearchRepository billTransactionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBillTransactionMockMvc;

    private BillTransaction billTransaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BillTransactionResource billTransactionResource = new BillTransactionResource();
        ReflectionTestUtils.setField(billTransactionResource, "billTransactionService", billTransactionService);
        this.restBillTransactionMockMvc = MockMvcBuilders.standaloneSetup(billTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillTransaction createEntity(EntityManager em) {
        BillTransaction billTransaction = new BillTransaction()
                .date(DEFAULT_DATE)
                .amount(DEFAULT_AMOUNT)
                .dayOut(DEFAULT_DAY_OUT)
                .fromAccount(DEFAULT_FROM_ACCOUNT)
                .automatic(DEFAULT_AUTOMATIC);
        return billTransaction;
    }

    @Before
    public void initTest() {
        billTransactionSearchRepository.deleteAll();
        billTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createBillTransaction() throws Exception {
        int databaseSizeBeforeCreate = billTransactionRepository.findAll().size();

        // Create the BillTransaction
        BillTransactionDTO billTransactionDTO = billTransactionMapper.billTransactionToBillTransactionDTO(billTransaction);

        restBillTransactionMockMvc.perform(post("/api/bill-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billTransactionDTO)))
                .andExpect(status().isCreated());

        // Validate the BillTransaction in the database
        List<BillTransaction> billTransactions = billTransactionRepository.findAll();
        assertThat(billTransactions).hasSize(databaseSizeBeforeCreate + 1);
        BillTransaction testBillTransaction = billTransactions.get(billTransactions.size() - 1);
        assertThat(testBillTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBillTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testBillTransaction.getDayOut()).isEqualTo(DEFAULT_DAY_OUT);
        assertThat(testBillTransaction.getFromAccount()).isEqualTo(DEFAULT_FROM_ACCOUNT);
        assertThat(testBillTransaction.isAutomatic()).isEqualTo(DEFAULT_AUTOMATIC);

        // Validate the BillTransaction in ElasticSearch
        BillTransaction billTransactionEs = billTransactionSearchRepository.findOne(testBillTransaction.getId());
        assertThat(billTransactionEs).isEqualToComparingFieldByField(testBillTransaction);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = billTransactionRepository.findAll().size();
        // set the field null
        billTransaction.setDate(null);

        // Create the BillTransaction, which fails.
        BillTransactionDTO billTransactionDTO = billTransactionMapper.billTransactionToBillTransactionDTO(billTransaction);

        restBillTransactionMockMvc.perform(post("/api/bill-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<BillTransaction> billTransactions = billTransactionRepository.findAll();
        assertThat(billTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBillTransactions() throws Exception {
        // Initialize the database
        billTransactionRepository.saveAndFlush(billTransaction);

        // Get all the billTransactions
        restBillTransactionMockMvc.perform(get("/api/bill-transactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(billTransaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].dayOut").value(hasItem(DEFAULT_DAY_OUT)))
                .andExpect(jsonPath("$.[*].fromAccount").value(hasItem(DEFAULT_FROM_ACCOUNT.toString())))
                .andExpect(jsonPath("$.[*].automatic").value(hasItem(DEFAULT_AUTOMATIC.booleanValue())));
    }

    @Test
    @Transactional
    public void getBillTransaction() throws Exception {
        // Initialize the database
        billTransactionRepository.saveAndFlush(billTransaction);

        // Get the billTransaction
        restBillTransactionMockMvc.perform(get("/api/bill-transactions/{id}", billTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(billTransaction.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.dayOut").value(DEFAULT_DAY_OUT))
            .andExpect(jsonPath("$.fromAccount").value(DEFAULT_FROM_ACCOUNT.toString()))
            .andExpect(jsonPath("$.automatic").value(DEFAULT_AUTOMATIC.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBillTransaction() throws Exception {
        // Get the billTransaction
        restBillTransactionMockMvc.perform(get("/api/bill-transactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillTransaction() throws Exception {
        // Initialize the database
        billTransactionRepository.saveAndFlush(billTransaction);
        billTransactionSearchRepository.save(billTransaction);
        int databaseSizeBeforeUpdate = billTransactionRepository.findAll().size();

        // Update the billTransaction
        BillTransaction updatedBillTransaction = billTransactionRepository.findOne(billTransaction.getId());
        updatedBillTransaction
                .date(UPDATED_DATE)
                .amount(UPDATED_AMOUNT)
                .dayOut(UPDATED_DAY_OUT)
                .fromAccount(UPDATED_FROM_ACCOUNT)
                .automatic(UPDATED_AUTOMATIC);
        BillTransactionDTO billTransactionDTO = billTransactionMapper.billTransactionToBillTransactionDTO(updatedBillTransaction);

        restBillTransactionMockMvc.perform(put("/api/bill-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billTransactionDTO)))
                .andExpect(status().isOk());

        // Validate the BillTransaction in the database
        List<BillTransaction> billTransactions = billTransactionRepository.findAll();
        assertThat(billTransactions).hasSize(databaseSizeBeforeUpdate);
        BillTransaction testBillTransaction = billTransactions.get(billTransactions.size() - 1);
        assertThat(testBillTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBillTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testBillTransaction.getDayOut()).isEqualTo(UPDATED_DAY_OUT);
        assertThat(testBillTransaction.getFromAccount()).isEqualTo(UPDATED_FROM_ACCOUNT);
        assertThat(testBillTransaction.isAutomatic()).isEqualTo(UPDATED_AUTOMATIC);

        // Validate the BillTransaction in ElasticSearch
        BillTransaction billTransactionEs = billTransactionSearchRepository.findOne(testBillTransaction.getId());
        assertThat(billTransactionEs).isEqualToComparingFieldByField(testBillTransaction);
    }

    @Test
    @Transactional
    public void deleteBillTransaction() throws Exception {
        // Initialize the database
        billTransactionRepository.saveAndFlush(billTransaction);
        billTransactionSearchRepository.save(billTransaction);
        int databaseSizeBeforeDelete = billTransactionRepository.findAll().size();

        // Get the billTransaction
        restBillTransactionMockMvc.perform(delete("/api/bill-transactions/{id}", billTransaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean billTransactionExistsInEs = billTransactionSearchRepository.exists(billTransaction.getId());
        assertThat(billTransactionExistsInEs).isFalse();

        // Validate the database is empty
        List<BillTransaction> billTransactions = billTransactionRepository.findAll();
        assertThat(billTransactions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBillTransaction() throws Exception {
        // Initialize the database
        billTransactionRepository.saveAndFlush(billTransaction);
        billTransactionSearchRepository.save(billTransaction);

        // Search the billTransaction
        restBillTransactionMockMvc.perform(get("/api/_search/bill-transactions?query=id:" + billTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dayOut").value(hasItem(DEFAULT_DAY_OUT)))
            .andExpect(jsonPath("$.[*].fromAccount").value(hasItem(DEFAULT_FROM_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].automatic").value(hasItem(DEFAULT_AUTOMATIC.booleanValue())));
    }
}
