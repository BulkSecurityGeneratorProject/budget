package com.budget.service.impl;

import com.budget.service.AmexTransactionService;
import com.budget.domain.AmexTransaction;
import com.budget.repository.AmexTransactionRepository;
import com.budget.repository.search.AmexTransactionSearchRepository;
import com.budget.service.dto.AmexTransactionDTO;
import com.budget.service.mapper.AmexTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AmexTransaction.
 */
@Service
@Transactional
public class AmexTransactionServiceImpl implements AmexTransactionService{

    private final Logger log = LoggerFactory.getLogger(AmexTransactionServiceImpl.class);
    
    @Inject
    private AmexTransactionRepository amexTransactionRepository;

    @Inject
    private AmexTransactionMapper amexTransactionMapper;

    @Inject
    private AmexTransactionSearchRepository amexTransactionSearchRepository;

    /**
     * Save a amexTransaction.
     *
     * @param amexTransactionDTO the entity to save
     * @return the persisted entity
     */
    public AmexTransactionDTO save(AmexTransactionDTO amexTransactionDTO) {
        log.debug("Request to save AmexTransaction : {}", amexTransactionDTO);
        AmexTransaction amexTransaction = amexTransactionMapper.amexTransactionDTOToAmexTransaction(amexTransactionDTO);
        amexTransaction = amexTransactionRepository.save(amexTransaction);
        AmexTransactionDTO result = amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction);
        amexTransactionSearchRepository.save(amexTransaction);
        return result;
    }

    /**
     *  Get all the amexTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AmexTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AmexTransactions");
        Page<AmexTransaction> result = amexTransactionRepository.findAll(pageable);
        return result.map(amexTransaction -> amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction));
    }

    /**
     *  Get one amexTransaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AmexTransactionDTO findOne(Long id) {
        log.debug("Request to get AmexTransaction : {}", id);
        AmexTransaction amexTransaction = amexTransactionRepository.findOne(id);
        AmexTransactionDTO amexTransactionDTO = amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction);
        return amexTransactionDTO;
    }

    /**
     *  Delete the  amexTransaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AmexTransaction : {}", id);
        amexTransactionRepository.delete(id);
        amexTransactionSearchRepository.delete(id);
    }

    /**
     * Search for the amexTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AmexTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AmexTransactions for query {}", query);
        Page<AmexTransaction> result = amexTransactionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(amexTransaction -> amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction));
    }
}
