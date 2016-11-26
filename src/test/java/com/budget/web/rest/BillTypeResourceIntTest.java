package com.budget.web.rest;

import com.budget.BudgetApp;

import com.budget.domain.BillType;
import com.budget.repository.BillTypeRepository;
import com.budget.repository.search.BillTypeSearchRepository;
import com.budget.service.dto.BillTypeDTO;
import com.budget.service.mapper.BillTypeMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.budget.domain.enumeration.BillMainType;
/**
 * Test class for the BillTypeResource REST controller.
 *
 * @see BillTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class BillTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final BillMainType DEFAULT_MAIN_TYPE = BillMainType.CREDIT_CARD;
    private static final BillMainType UPDATED_MAIN_TYPE = BillMainType.STUDENT_LOAN;

    @Inject
    private BillTypeRepository billTypeRepository;

    @Inject
    private BillTypeMapper billTypeMapper;

    @Inject
    private BillTypeSearchRepository billTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBillTypeMockMvc;

    private BillType billType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BillTypeResource billTypeResource = new BillTypeResource();
        ReflectionTestUtils.setField(billTypeResource, "billTypeSearchRepository", billTypeSearchRepository);
        ReflectionTestUtils.setField(billTypeResource, "billTypeRepository", billTypeRepository);
        ReflectionTestUtils.setField(billTypeResource, "billTypeMapper", billTypeMapper);
        this.restBillTypeMockMvc = MockMvcBuilders.standaloneSetup(billTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillType createEntity(EntityManager em) {
        BillType billType = new BillType()
                .name(DEFAULT_NAME)
                .mainType(DEFAULT_MAIN_TYPE);
        return billType;
    }

    @Before
    public void initTest() {
        billTypeSearchRepository.deleteAll();
        billType = createEntity(em);
    }

    @Test
    @Transactional
    public void createBillType() throws Exception {
        int databaseSizeBeforeCreate = billTypeRepository.findAll().size();

        // Create the BillType
        BillTypeDTO billTypeDTO = billTypeMapper.billTypeToBillTypeDTO(billType);

        restBillTypeMockMvc.perform(post("/api/bill-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billTypeDTO)))
                .andExpect(status().isCreated());

        // Validate the BillType in the database
        List<BillType> billTypes = billTypeRepository.findAll();
        assertThat(billTypes).hasSize(databaseSizeBeforeCreate + 1);
        BillType testBillType = billTypes.get(billTypes.size() - 1);
        assertThat(testBillType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBillType.getMainType()).isEqualTo(DEFAULT_MAIN_TYPE);

        // Validate the BillType in ElasticSearch
        BillType billTypeEs = billTypeSearchRepository.findOne(testBillType.getId());
        assertThat(billTypeEs).isEqualToComparingFieldByField(testBillType);
    }

    @Test
    @Transactional
    public void getAllBillTypes() throws Exception {
        // Initialize the database
        billTypeRepository.saveAndFlush(billType);

        // Get all the billTypes
        restBillTypeMockMvc.perform(get("/api/bill-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(billType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].mainType").value(hasItem(DEFAULT_MAIN_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getBillType() throws Exception {
        // Initialize the database
        billTypeRepository.saveAndFlush(billType);

        // Get the billType
        restBillTypeMockMvc.perform(get("/api/bill-types/{id}", billType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(billType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.mainType").value(DEFAULT_MAIN_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBillType() throws Exception {
        // Get the billType
        restBillTypeMockMvc.perform(get("/api/bill-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillType() throws Exception {
        // Initialize the database
        billTypeRepository.saveAndFlush(billType);
        billTypeSearchRepository.save(billType);
        int databaseSizeBeforeUpdate = billTypeRepository.findAll().size();

        // Update the billType
        BillType updatedBillType = billTypeRepository.findOne(billType.getId());
        updatedBillType
                .name(UPDATED_NAME)
                .mainType(UPDATED_MAIN_TYPE);
        BillTypeDTO billTypeDTO = billTypeMapper.billTypeToBillTypeDTO(updatedBillType);

        restBillTypeMockMvc.perform(put("/api/bill-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billTypeDTO)))
                .andExpect(status().isOk());

        // Validate the BillType in the database
        List<BillType> billTypes = billTypeRepository.findAll();
        assertThat(billTypes).hasSize(databaseSizeBeforeUpdate);
        BillType testBillType = billTypes.get(billTypes.size() - 1);
        assertThat(testBillType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBillType.getMainType()).isEqualTo(UPDATED_MAIN_TYPE);

        // Validate the BillType in ElasticSearch
        BillType billTypeEs = billTypeSearchRepository.findOne(testBillType.getId());
        assertThat(billTypeEs).isEqualToComparingFieldByField(testBillType);
    }

    @Test
    @Transactional
    public void deleteBillType() throws Exception {
        // Initialize the database
        billTypeRepository.saveAndFlush(billType);
        billTypeSearchRepository.save(billType);
        int databaseSizeBeforeDelete = billTypeRepository.findAll().size();

        // Get the billType
        restBillTypeMockMvc.perform(delete("/api/bill-types/{id}", billType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean billTypeExistsInEs = billTypeSearchRepository.exists(billType.getId());
        assertThat(billTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<BillType> billTypes = billTypeRepository.findAll();
        assertThat(billTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBillType() throws Exception {
        // Initialize the database
        billTypeRepository.saveAndFlush(billType);
        billTypeSearchRepository.save(billType);

        // Search the billType
        restBillTypeMockMvc.perform(get("/api/_search/bill-types?query=id:" + billType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].mainType").value(hasItem(DEFAULT_MAIN_TYPE.toString())));
    }
}
