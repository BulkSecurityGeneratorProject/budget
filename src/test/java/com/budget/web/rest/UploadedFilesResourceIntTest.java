package com.budget.web.rest;

import com.budget.BudgetApp;

import com.budget.domain.UploadedFiles;
import com.budget.repository.UploadedFilesRepository;
import com.budget.service.UploadedFilesService;
import com.budget.repository.search.UploadedFilesSearchRepository;
import com.budget.service.dto.UploadedFilesDTO;
import com.budget.service.mapper.UploadedFilesMapper;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.budget.domain.enumeration.Bank;
import com.budget.domain.enumeration.AccountType;
/**
 * Test class for the UploadedFilesResource REST controller.
 *
 * @see UploadedFilesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class UploadedFilesResourceIntTest {

    private static final LocalDate DEFAULT_UPLOAD_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPLOAD_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Bank DEFAULT_BANK = Bank.ALLY;
    private static final Bank UPDATED_BANK = Bank.WELLS_FARGO;

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.SAVINGS;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.CHECKING;

    @Inject
    private UploadedFilesRepository uploadedFilesRepository;

    @Inject
    private UploadedFilesMapper uploadedFilesMapper;

    @Inject
    private UploadedFilesService uploadedFilesService;

    @Inject
    private UploadedFilesSearchRepository uploadedFilesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUploadedFilesMockMvc;

    private UploadedFiles uploadedFiles;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UploadedFilesResource uploadedFilesResource = new UploadedFilesResource();
        ReflectionTestUtils.setField(uploadedFilesResource, "uploadedFilesService", uploadedFilesService);
        this.restUploadedFilesMockMvc = MockMvcBuilders.standaloneSetup(uploadedFilesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UploadedFiles createEntity(EntityManager em) {
        UploadedFiles uploadedFiles = new UploadedFiles()
                .uploadDate(DEFAULT_UPLOAD_DATE)
                .bank(DEFAULT_BANK)
                .file(DEFAULT_FILE)
                .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
                .accountType(DEFAULT_ACCOUNT_TYPE);
        return uploadedFiles;
    }

    @Before
    public void initTest() {
        uploadedFilesSearchRepository.deleteAll();
        uploadedFiles = createEntity(em);
    }

    @Test
    @Transactional
    public void createUploadedFiles() throws Exception {
        int databaseSizeBeforeCreate = uploadedFilesRepository.findAll().size();

        // Create the UploadedFiles
        UploadedFilesDTO uploadedFilesDTO = uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(uploadedFiles);

        restUploadedFilesMockMvc.perform(post("/api/uploaded-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(uploadedFilesDTO)))
                .andExpect(status().isCreated());

        // Validate the UploadedFiles in the database
        List<UploadedFiles> uploadedFiles = uploadedFilesRepository.findAll();
        assertThat(uploadedFiles).hasSize(databaseSizeBeforeCreate + 1);
        UploadedFiles testUploadedFiles = uploadedFiles.get(uploadedFiles.size() - 1);
        assertThat(testUploadedFiles.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
        assertThat(testUploadedFiles.getBank()).isEqualTo(DEFAULT_BANK);
        assertThat(testUploadedFiles.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testUploadedFiles.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testUploadedFiles.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);

        // Validate the UploadedFiles in ElasticSearch
        UploadedFiles uploadedFilesEs = uploadedFilesSearchRepository.findOne(testUploadedFiles.getId());
        assertThat(uploadedFilesEs).isEqualToComparingFieldByField(testUploadedFiles);
    }

    @Test
    @Transactional
    public void checkUploadDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = uploadedFilesRepository.findAll().size();
        // set the field null
        uploadedFiles.setUploadDate(null);

        // Create the UploadedFiles, which fails.
        UploadedFilesDTO uploadedFilesDTO = uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(uploadedFiles);

        restUploadedFilesMockMvc.perform(post("/api/uploaded-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(uploadedFilesDTO)))
                .andExpect(status().isBadRequest());

        List<UploadedFiles> uploadedFiles = uploadedFilesRepository.findAll();
        assertThat(uploadedFiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBankIsRequired() throws Exception {
        int databaseSizeBeforeTest = uploadedFilesRepository.findAll().size();
        // set the field null
        uploadedFiles.setBank(null);

        // Create the UploadedFiles, which fails.
        UploadedFilesDTO uploadedFilesDTO = uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(uploadedFiles);

        restUploadedFilesMockMvc.perform(post("/api/uploaded-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(uploadedFilesDTO)))
                .andExpect(status().isBadRequest());

        List<UploadedFiles> uploadedFiles = uploadedFilesRepository.findAll();
        assertThat(uploadedFiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = uploadedFilesRepository.findAll().size();
        // set the field null
        uploadedFiles.setFile(null);

        // Create the UploadedFiles, which fails.
        UploadedFilesDTO uploadedFilesDTO = uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(uploadedFiles);

        restUploadedFilesMockMvc.perform(post("/api/uploaded-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(uploadedFilesDTO)))
                .andExpect(status().isBadRequest());

        List<UploadedFiles> uploadedFiles = uploadedFilesRepository.findAll();
        assertThat(uploadedFiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = uploadedFilesRepository.findAll().size();
        // set the field null
        uploadedFiles.setAccountType(null);

        // Create the UploadedFiles, which fails.
        UploadedFilesDTO uploadedFilesDTO = uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(uploadedFiles);

        restUploadedFilesMockMvc.perform(post("/api/uploaded-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(uploadedFilesDTO)))
                .andExpect(status().isBadRequest());

        List<UploadedFiles> uploadedFiles = uploadedFilesRepository.findAll();
        assertThat(uploadedFiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUploadedFiles() throws Exception {
        // Initialize the database
        uploadedFilesRepository.saveAndFlush(uploadedFiles);

        // Get all the uploadedFiles
        restUploadedFilesMockMvc.perform(get("/api/uploaded-files?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(uploadedFiles.getId().intValue())))
                .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
                .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK.toString())))
                .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
                .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getUploadedFiles() throws Exception {
        // Initialize the database
        uploadedFilesRepository.saveAndFlush(uploadedFiles);

        // Get the uploadedFiles
        restUploadedFilesMockMvc.perform(get("/api/uploaded-files/{id}", uploadedFiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(uploadedFiles.getId().intValue()))
            .andExpect(jsonPath("$.uploadDate").value(DEFAULT_UPLOAD_DATE.toString()))
            .andExpect(jsonPath("$.bank").value(DEFAULT_BANK.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUploadedFiles() throws Exception {
        // Get the uploadedFiles
        restUploadedFilesMockMvc.perform(get("/api/uploaded-files/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUploadedFiles() throws Exception {
        // Initialize the database
        uploadedFilesRepository.saveAndFlush(uploadedFiles);
        uploadedFilesSearchRepository.save(uploadedFiles);
        int databaseSizeBeforeUpdate = uploadedFilesRepository.findAll().size();

        // Update the uploadedFiles
        UploadedFiles updatedUploadedFiles = uploadedFilesRepository.findOne(uploadedFiles.getId());
        updatedUploadedFiles
                .uploadDate(UPDATED_UPLOAD_DATE)
                .bank(UPDATED_BANK)
                .file(UPDATED_FILE)
                .fileContentType(UPDATED_FILE_CONTENT_TYPE)
                .accountType(UPDATED_ACCOUNT_TYPE);
        UploadedFilesDTO uploadedFilesDTO = uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(updatedUploadedFiles);

        restUploadedFilesMockMvc.perform(put("/api/uploaded-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(uploadedFilesDTO)))
                .andExpect(status().isOk());

        // Validate the UploadedFiles in the database
        List<UploadedFiles> uploadedFiles = uploadedFilesRepository.findAll();
        assertThat(uploadedFiles).hasSize(databaseSizeBeforeUpdate);
        UploadedFiles testUploadedFiles = uploadedFiles.get(uploadedFiles.size() - 1);
        assertThat(testUploadedFiles.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testUploadedFiles.getBank()).isEqualTo(UPDATED_BANK);
        assertThat(testUploadedFiles.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testUploadedFiles.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testUploadedFiles.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);

        // Validate the UploadedFiles in ElasticSearch
        UploadedFiles uploadedFilesEs = uploadedFilesSearchRepository.findOne(testUploadedFiles.getId());
        assertThat(uploadedFilesEs).isEqualToComparingFieldByField(testUploadedFiles);
    }

    @Test
    @Transactional
    public void deleteUploadedFiles() throws Exception {
        // Initialize the database
        uploadedFilesRepository.saveAndFlush(uploadedFiles);
        uploadedFilesSearchRepository.save(uploadedFiles);
        int databaseSizeBeforeDelete = uploadedFilesRepository.findAll().size();

        // Get the uploadedFiles
        restUploadedFilesMockMvc.perform(delete("/api/uploaded-files/{id}", uploadedFiles.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean uploadedFilesExistsInEs = uploadedFilesSearchRepository.exists(uploadedFiles.getId());
        assertThat(uploadedFilesExistsInEs).isFalse();

        // Validate the database is empty
        List<UploadedFiles> uploadedFiles = uploadedFilesRepository.findAll();
        assertThat(uploadedFiles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUploadedFiles() throws Exception {
        // Initialize the database
        uploadedFilesRepository.saveAndFlush(uploadedFiles);
        uploadedFilesSearchRepository.save(uploadedFiles);

        // Search the uploadedFiles
        restUploadedFilesMockMvc.perform(get("/api/_search/uploaded-files?query=id:" + uploadedFiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uploadedFiles.getId().intValue())))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())));
    }
}
