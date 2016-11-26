package com.budget.web.rest;

import com.budget.BudgetApp;

import com.budget.domain.SinkingFundTransaction;
import com.budget.repository.SinkingFundTransactionRepository;
import com.budget.service.SinkingFundTransactionService;
import com.budget.repository.search.SinkingFundTransactionSearchRepository;
import com.budget.service.dto.SinkingFundTransactionDTO;
import com.budget.service.mapper.SinkingFundTransactionMapper;

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

import com.budget.domain.enumeration.SinkingFundType;
/**
 * Test class for the SinkingFundTransactionResource REST controller.
 *
 * @see SinkingFundTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class SinkingFundTransactionResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final SinkingFundType DEFAULT_TYPE = SinkingFundType.HOUSE;
    private static final SinkingFundType UPDATED_TYPE = SinkingFundType.CAR;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private SinkingFundTransactionRepository sinkingFundTransactionRepository;

    @Inject
    private SinkingFundTransactionMapper sinkingFundTransactionMapper;

    @Inject
    private SinkingFundTransactionService sinkingFundTransactionService;

    @Inject
    private SinkingFundTransactionSearchRepository sinkingFundTransactionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSinkingFundTransactionMockMvc;

    private SinkingFundTransaction sinkingFundTransaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SinkingFundTransactionResource sinkingFundTransactionResource = new SinkingFundTransactionResource();
        ReflectionTestUtils.setField(sinkingFundTransactionResource, "sinkingFundTransactionService", sinkingFundTransactionService);
        this.restSinkingFundTransactionMockMvc = MockMvcBuilders.standaloneSetup(sinkingFundTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SinkingFundTransaction createEntity(EntityManager em) {
        SinkingFundTransaction sinkingFundTransaction = new SinkingFundTransaction()
                .date(DEFAULT_DATE)
                .type(DEFAULT_TYPE)
                .amount(DEFAULT_AMOUNT);
        return sinkingFundTransaction;
    }

    @Before
    public void initTest() {
        sinkingFundTransactionSearchRepository.deleteAll();
        sinkingFundTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createSinkingFundTransaction() throws Exception {
        int databaseSizeBeforeCreate = sinkingFundTransactionRepository.findAll().size();

        // Create the SinkingFundTransaction
        SinkingFundTransactionDTO sinkingFundTransactionDTO = sinkingFundTransactionMapper.sinkingFundTransactionToSinkingFundTransactionDTO(sinkingFundTransaction);

        restSinkingFundTransactionMockMvc.perform(post("/api/sinking-fund-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sinkingFundTransactionDTO)))
                .andExpect(status().isCreated());

        // Validate the SinkingFundTransaction in the database
        List<SinkingFundTransaction> sinkingFundTransactions = sinkingFundTransactionRepository.findAll();
        assertThat(sinkingFundTransactions).hasSize(databaseSizeBeforeCreate + 1);
        SinkingFundTransaction testSinkingFundTransaction = sinkingFundTransactions.get(sinkingFundTransactions.size() - 1);
        assertThat(testSinkingFundTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSinkingFundTransaction.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSinkingFundTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the SinkingFundTransaction in ElasticSearch
        SinkingFundTransaction sinkingFundTransactionEs = sinkingFundTransactionSearchRepository.findOne(testSinkingFundTransaction.getId());
        assertThat(sinkingFundTransactionEs).isEqualToComparingFieldByField(testSinkingFundTransaction);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = sinkingFundTransactionRepository.findAll().size();
        // set the field null
        sinkingFundTransaction.setDate(null);

        // Create the SinkingFundTransaction, which fails.
        SinkingFundTransactionDTO sinkingFundTransactionDTO = sinkingFundTransactionMapper.sinkingFundTransactionToSinkingFundTransactionDTO(sinkingFundTransaction);

        restSinkingFundTransactionMockMvc.perform(post("/api/sinking-fund-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sinkingFundTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<SinkingFundTransaction> sinkingFundTransactions = sinkingFundTransactionRepository.findAll();
        assertThat(sinkingFundTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sinkingFundTransactionRepository.findAll().size();
        // set the field null
        sinkingFundTransaction.setType(null);

        // Create the SinkingFundTransaction, which fails.
        SinkingFundTransactionDTO sinkingFundTransactionDTO = sinkingFundTransactionMapper.sinkingFundTransactionToSinkingFundTransactionDTO(sinkingFundTransaction);

        restSinkingFundTransactionMockMvc.perform(post("/api/sinking-fund-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sinkingFundTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<SinkingFundTransaction> sinkingFundTransactions = sinkingFundTransactionRepository.findAll();
        assertThat(sinkingFundTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = sinkingFundTransactionRepository.findAll().size();
        // set the field null
        sinkingFundTransaction.setAmount(null);

        // Create the SinkingFundTransaction, which fails.
        SinkingFundTransactionDTO sinkingFundTransactionDTO = sinkingFundTransactionMapper.sinkingFundTransactionToSinkingFundTransactionDTO(sinkingFundTransaction);

        restSinkingFundTransactionMockMvc.perform(post("/api/sinking-fund-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sinkingFundTransactionDTO)))
                .andExpect(status().isBadRequest());

        List<SinkingFundTransaction> sinkingFundTransactions = sinkingFundTransactionRepository.findAll();
        assertThat(sinkingFundTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSinkingFundTransactions() throws Exception {
        // Initialize the database
        sinkingFundTransactionRepository.saveAndFlush(sinkingFundTransaction);

        // Get all the sinkingFundTransactions
        restSinkingFundTransactionMockMvc.perform(get("/api/sinking-fund-transactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sinkingFundTransaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getSinkingFundTransaction() throws Exception {
        // Initialize the database
        sinkingFundTransactionRepository.saveAndFlush(sinkingFundTransaction);

        // Get the sinkingFundTransaction
        restSinkingFundTransactionMockMvc.perform(get("/api/sinking-fund-transactions/{id}", sinkingFundTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sinkingFundTransaction.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSinkingFundTransaction() throws Exception {
        // Get the sinkingFundTransaction
        restSinkingFundTransactionMockMvc.perform(get("/api/sinking-fund-transactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSinkingFundTransaction() throws Exception {
        // Initialize the database
        sinkingFundTransactionRepository.saveAndFlush(sinkingFundTransaction);
        sinkingFundTransactionSearchRepository.save(sinkingFundTransaction);
        int databaseSizeBeforeUpdate = sinkingFundTransactionRepository.findAll().size();

        // Update the sinkingFundTransaction
        SinkingFundTransaction updatedSinkingFundTransaction = sinkingFundTransactionRepository.findOne(sinkingFundTransaction.getId());
        updatedSinkingFundTransaction
                .date(UPDATED_DATE)
                .type(UPDATED_TYPE)
                .amount(UPDATED_AMOUNT);
        SinkingFundTransactionDTO sinkingFundTransactionDTO = sinkingFundTransactionMapper.sinkingFundTransactionToSinkingFundTransactionDTO(updatedSinkingFundTransaction);

        restSinkingFundTransactionMockMvc.perform(put("/api/sinking-fund-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sinkingFundTransactionDTO)))
                .andExpect(status().isOk());

        // Validate the SinkingFundTransaction in the database
        List<SinkingFundTransaction> sinkingFundTransactions = sinkingFundTransactionRepository.findAll();
        assertThat(sinkingFundTransactions).hasSize(databaseSizeBeforeUpdate);
        SinkingFundTransaction testSinkingFundTransaction = sinkingFundTransactions.get(sinkingFundTransactions.size() - 1);
        assertThat(testSinkingFundTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSinkingFundTransaction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSinkingFundTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the SinkingFundTransaction in ElasticSearch
        SinkingFundTransaction sinkingFundTransactionEs = sinkingFundTransactionSearchRepository.findOne(testSinkingFundTransaction.getId());
        assertThat(sinkingFundTransactionEs).isEqualToComparingFieldByField(testSinkingFundTransaction);
    }

    @Test
    @Transactional
    public void deleteSinkingFundTransaction() throws Exception {
        // Initialize the database
        sinkingFundTransactionRepository.saveAndFlush(sinkingFundTransaction);
        sinkingFundTransactionSearchRepository.save(sinkingFundTransaction);
        int databaseSizeBeforeDelete = sinkingFundTransactionRepository.findAll().size();

        // Get the sinkingFundTransaction
        restSinkingFundTransactionMockMvc.perform(delete("/api/sinking-fund-transactions/{id}", sinkingFundTransaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean sinkingFundTransactionExistsInEs = sinkingFundTransactionSearchRepository.exists(sinkingFundTransaction.getId());
        assertThat(sinkingFundTransactionExistsInEs).isFalse();

        // Validate the database is empty
        List<SinkingFundTransaction> sinkingFundTransactions = sinkingFundTransactionRepository.findAll();
        assertThat(sinkingFundTransactions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSinkingFundTransaction() throws Exception {
        // Initialize the database
        sinkingFundTransactionRepository.saveAndFlush(sinkingFundTransaction);
        sinkingFundTransactionSearchRepository.save(sinkingFundTransaction);

        // Search the sinkingFundTransaction
        restSinkingFundTransactionMockMvc.perform(get("/api/_search/sinking-fund-transactions?query=id:" + sinkingFundTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sinkingFundTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }
}
