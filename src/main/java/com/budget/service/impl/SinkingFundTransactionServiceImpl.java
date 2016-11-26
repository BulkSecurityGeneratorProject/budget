package com.budget.service.impl;

import com.budget.service.SinkingFundTransactionService;
import com.budget.domain.SinkingFundTransaction;
import com.budget.repository.SinkingFundTransactionRepository;
import com.budget.repository.search.SinkingFundTransactionSearchRepository;
import com.budget.service.dto.SinkingFundTransactionDTO;
import com.budget.service.mapper.SinkingFundTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SinkingFundTransaction.
 */
@Service
@Transactional
public class SinkingFundTransactionServiceImpl implements SinkingFundTransactionService{

    private final Logger log = LoggerFactory.getLogger(SinkingFundTransactionServiceImpl.class);
    
    @Inject
    private SinkingFundTransactionRepository sinkingFundTransactionRepository;

    @Inject
    private SinkingFundTransactionMapper sinkingFundTransactionMapper;

    @Inject
    private SinkingFundTransactionSearchRepository sinkingFundTransactionSearchRepository;

    /**
     * Save a sinkingFundTransaction.
     *
     * @param sinkingFundTransactionDTO the entity to save
     * @return the persisted entity
     */
    public SinkingFundTransactionDTO save(SinkingFundTransactionDTO sinkingFundTransactionDTO) {
        log.debug("Request to save SinkingFundTransaction : {}", sinkingFundTransactionDTO);
        SinkingFundTransaction sinkingFundTransaction = sinkingFundTransactionMapper.sinkingFundTransactionDTOToSinkingFundTransaction(sinkingFundTransactionDTO);
        sinkingFundTransaction = sinkingFundTransactionRepository.save(sinkingFundTransaction);
        SinkingFundTransactionDTO result = sinkingFundTransactionMapper.sinkingFundTransactionToSinkingFundTransactionDTO(sinkingFundTransaction);
        sinkingFundTransactionSearchRepository.save(sinkingFundTransaction);
        return result;
    }

    /**
     *  Get all the sinkingFundTransactions.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SinkingFundTransactionDTO> findAll() {
        log.debug("Request to get all SinkingFundTransactions");
        List<SinkingFundTransactionDTO> result = sinkingFundTransactionRepository.findAll().stream()
            .map(sinkingFundTransactionMapper::sinkingFundTransactionToSinkingFundTransactionDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one sinkingFundTransaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SinkingFundTransactionDTO findOne(Long id) {
        log.debug("Request to get SinkingFundTransaction : {}", id);
        SinkingFundTransaction sinkingFundTransaction = sinkingFundTransactionRepository.findOne(id);
        SinkingFundTransactionDTO sinkingFundTransactionDTO = sinkingFundTransactionMapper.sinkingFundTransactionToSinkingFundTransactionDTO(sinkingFundTransaction);
        return sinkingFundTransactionDTO;
    }

    /**
     *  Delete the  sinkingFundTransaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SinkingFundTransaction : {}", id);
        sinkingFundTransactionRepository.delete(id);
        sinkingFundTransactionSearchRepository.delete(id);
    }

    /**
     * Search for the sinkingFundTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SinkingFundTransactionDTO> search(String query) {
        log.debug("Request to search SinkingFundTransactions for query {}", query);
        return StreamSupport
            .stream(sinkingFundTransactionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(sinkingFundTransactionMapper::sinkingFundTransactionToSinkingFundTransactionDTO)
            .collect(Collectors.toList());
    }
}
