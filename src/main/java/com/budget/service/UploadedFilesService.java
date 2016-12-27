package com.budget.service;

import com.budget.domain.UploadedFiles;
import com.budget.repository.UploadedFilesRepository;
import com.budget.repository.search.UploadedFilesSearchRepository;
import com.budget.service.dto.UploadedFilesDTO;
import com.budget.service.mapper.UploadedFilesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.LocalDate;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UploadedFiles.
 */
@Service
@Transactional
public class UploadedFilesService {

    private final Logger log = LoggerFactory.getLogger(UploadedFilesService.class);
    
    @Inject
    private UploadedFilesRepository uploadedFilesRepository;

    @Inject
    private UploadedFilesMapper uploadedFilesMapper;

    @Inject
    private UploadedFilesSearchRepository uploadedFilesSearchRepository;
    
    @Inject
    private AllyTransactionService allyTransactionService;
    
    @Inject
    private AmexTransactionService amexTransactionService;
    
    @Inject 
    private WellsFargoTransactionService wellsFargoTransactionService;

    /**
     * Save a uploadedFiles.
     *
     * @param uploadedFilesDTO the entity to save
     * @return the persisted entity
     */
    public UploadedFilesDTO save(UploadedFilesDTO uploadedFilesDTO) {
        log.debug("Request to save UploadedFiles : {}", uploadedFilesDTO);
        if (uploadedFilesDTO != null && uploadedFilesDTO.getUploadDate() == null) {
        	uploadedFilesDTO.setUploadDate(LocalDate.now());
        }
        UploadedFiles uploadedFiles = uploadedFilesMapper.uploadedFilesDTOToUploadedFiles(uploadedFilesDTO);
        uploadedFiles = uploadedFilesRepository.save(uploadedFiles);
        deligateBankParsingService(uploadedFiles);
        UploadedFilesDTO result = uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(uploadedFiles);
        uploadedFilesSearchRepository.save(uploadedFiles);
        return result;
    }
    
    private void deligateBankParsingService(UploadedFiles uploadedFile) {
    	switch (uploadedFile.getBank()) {
    	case ALLY:
    		allyTransactionService.parseCsvAndSave(uploadedFile);
    		break;
    	case AMEX:
    		amexTransactionService.parseCsvAndSave(uploadedFile);
    		break;
    	case WELLS_FARGO:
    		wellsFargoTransactionService.parseCsvAndSave(uploadedFile);
    		break;
    	default:
    		break;
    	}
    }

    /**
     *  Get all the uploadedFiles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<UploadedFilesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UploadedFiles");
        Page<UploadedFiles> result = uploadedFilesRepository.findAll(pageable);
        return result.map(uploadedFiles -> uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(uploadedFiles));
    }

    /**
     *  Get one uploadedFiles by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public UploadedFilesDTO findOne(Long id) {
        log.debug("Request to get UploadedFiles : {}", id);
        UploadedFiles uploadedFiles = uploadedFilesRepository.findOne(id);
        UploadedFilesDTO uploadedFilesDTO = uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(uploadedFiles);
        return uploadedFilesDTO;
    }

    /**
     *  Delete the  uploadedFiles by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UploadedFiles : {}", id);
        uploadedFilesRepository.delete(id);
        uploadedFilesSearchRepository.delete(id);
    }

    /**
     * Search for the uploadedFiles corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UploadedFilesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UploadedFiles for query {}", query);
        Page<UploadedFiles> result = uploadedFilesSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(uploadedFiles -> uploadedFilesMapper.uploadedFilesToUploadedFilesDTO(uploadedFiles));
    }
}
