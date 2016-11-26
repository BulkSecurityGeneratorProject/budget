package com.budget.web.rest;

import com.budget.BudgetApp;

import com.budget.domain.SpendingType;
import com.budget.repository.SpendingTypeRepository;
import com.budget.repository.search.SpendingTypeSearchRepository;
import com.budget.service.dto.SpendingTypeDTO;
import com.budget.service.mapper.SpendingTypeMapper;

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

import com.budget.domain.enumeration.SpendingMainType;
/**
 * Test class for the SpendingTypeResource REST controller.
 *
 * @see SpendingTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class SpendingTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final SpendingMainType DEFAULT_MAIN_TYPE = SpendingMainType.GROCERIES;
    private static final SpendingMainType UPDATED_MAIN_TYPE = SpendingMainType.SHOPPING;

    @Inject
    private SpendingTypeRepository spendingTypeRepository;

    @Inject
    private SpendingTypeMapper spendingTypeMapper;

    @Inject
    private SpendingTypeSearchRepository spendingTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSpendingTypeMockMvc;

    private SpendingType spendingType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpendingTypeResource spendingTypeResource = new SpendingTypeResource();
        ReflectionTestUtils.setField(spendingTypeResource, "spendingTypeSearchRepository", spendingTypeSearchRepository);
        ReflectionTestUtils.setField(spendingTypeResource, "spendingTypeRepository", spendingTypeRepository);
        ReflectionTestUtils.setField(spendingTypeResource, "spendingTypeMapper", spendingTypeMapper);
        this.restSpendingTypeMockMvc = MockMvcBuilders.standaloneSetup(spendingTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpendingType createEntity(EntityManager em) {
        SpendingType spendingType = new SpendingType()
                .name(DEFAULT_NAME)
                .mainType(DEFAULT_MAIN_TYPE);
        return spendingType;
    }

    @Before
    public void initTest() {
        spendingTypeSearchRepository.deleteAll();
        spendingType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpendingType() throws Exception {
        int databaseSizeBeforeCreate = spendingTypeRepository.findAll().size();

        // Create the SpendingType
        SpendingTypeDTO spendingTypeDTO = spendingTypeMapper.spendingTypeToSpendingTypeDTO(spendingType);

        restSpendingTypeMockMvc.perform(post("/api/spending-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spendingTypeDTO)))
                .andExpect(status().isCreated());

        // Validate the SpendingType in the database
        List<SpendingType> spendingTypes = spendingTypeRepository.findAll();
        assertThat(spendingTypes).hasSize(databaseSizeBeforeCreate + 1);
        SpendingType testSpendingType = spendingTypes.get(spendingTypes.size() - 1);
        assertThat(testSpendingType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpendingType.getMainType()).isEqualTo(DEFAULT_MAIN_TYPE);

        // Validate the SpendingType in ElasticSearch
        SpendingType spendingTypeEs = spendingTypeSearchRepository.findOne(testSpendingType.getId());
        assertThat(spendingTypeEs).isEqualToComparingFieldByField(testSpendingType);
    }

    @Test
    @Transactional
    public void getAllSpendingTypes() throws Exception {
        // Initialize the database
        spendingTypeRepository.saveAndFlush(spendingType);

        // Get all the spendingTypes
        restSpendingTypeMockMvc.perform(get("/api/spending-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(spendingType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].mainType").value(hasItem(DEFAULT_MAIN_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getSpendingType() throws Exception {
        // Initialize the database
        spendingTypeRepository.saveAndFlush(spendingType);

        // Get the spendingType
        restSpendingTypeMockMvc.perform(get("/api/spending-types/{id}", spendingType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spendingType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.mainType").value(DEFAULT_MAIN_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpendingType() throws Exception {
        // Get the spendingType
        restSpendingTypeMockMvc.perform(get("/api/spending-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpendingType() throws Exception {
        // Initialize the database
        spendingTypeRepository.saveAndFlush(spendingType);
        spendingTypeSearchRepository.save(spendingType);
        int databaseSizeBeforeUpdate = spendingTypeRepository.findAll().size();

        // Update the spendingType
        SpendingType updatedSpendingType = spendingTypeRepository.findOne(spendingType.getId());
        updatedSpendingType
                .name(UPDATED_NAME)
                .mainType(UPDATED_MAIN_TYPE);
        SpendingTypeDTO spendingTypeDTO = spendingTypeMapper.spendingTypeToSpendingTypeDTO(updatedSpendingType);

        restSpendingTypeMockMvc.perform(put("/api/spending-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spendingTypeDTO)))
                .andExpect(status().isOk());

        // Validate the SpendingType in the database
        List<SpendingType> spendingTypes = spendingTypeRepository.findAll();
        assertThat(spendingTypes).hasSize(databaseSizeBeforeUpdate);
        SpendingType testSpendingType = spendingTypes.get(spendingTypes.size() - 1);
        assertThat(testSpendingType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpendingType.getMainType()).isEqualTo(UPDATED_MAIN_TYPE);

        // Validate the SpendingType in ElasticSearch
        SpendingType spendingTypeEs = spendingTypeSearchRepository.findOne(testSpendingType.getId());
        assertThat(spendingTypeEs).isEqualToComparingFieldByField(testSpendingType);
    }

    @Test
    @Transactional
    public void deleteSpendingType() throws Exception {
        // Initialize the database
        spendingTypeRepository.saveAndFlush(spendingType);
        spendingTypeSearchRepository.save(spendingType);
        int databaseSizeBeforeDelete = spendingTypeRepository.findAll().size();

        // Get the spendingType
        restSpendingTypeMockMvc.perform(delete("/api/spending-types/{id}", spendingType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean spendingTypeExistsInEs = spendingTypeSearchRepository.exists(spendingType.getId());
        assertThat(spendingTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<SpendingType> spendingTypes = spendingTypeRepository.findAll();
        assertThat(spendingTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSpendingType() throws Exception {
        // Initialize the database
        spendingTypeRepository.saveAndFlush(spendingType);
        spendingTypeSearchRepository.save(spendingType);

        // Search the spendingType
        restSpendingTypeMockMvc.perform(get("/api/_search/spending-types?query=id:" + spendingType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spendingType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].mainType").value(hasItem(DEFAULT_MAIN_TYPE.toString())));
    }
}
