package com.budget.service.impl;

import com.budget.service.SpendingTransactionService;
import com.budget.domain.SpendingTransaction;
import com.budget.repository.SpendingTransactionRepository;
import com.budget.repository.search.SpendingTransactionSearchRepository;
import com.budget.service.dto.SpendingTransactionDTO;
import com.budget.service.mapper.SpendingTransactionMapper;
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
 * Service Implementation for managing SpendingTransaction.
 */
@Service
@Transactional
public class SpendingTransactionServiceImpl implements SpendingTransactionService{

    private final Logger log = LoggerFactory.getLogger(SpendingTransactionServiceImpl.class);
    
    @Inject
    private SpendingTransactionRepository spendingTransactionRepository;

    @Inject
    private SpendingTransactionMapper spendingTransactionMapper;

    @Inject
    private SpendingTransactionSearchRepository spendingTransactionSearchRepository;

    /**
     * Save a spendingTransaction.
     *
     * @param spendingTransactionDTO the entity to save
     * @return the persisted entity
     */
    public SpendingTransactionDTO save(SpendingTransactionDTO spendingTransactionDTO) {
        log.debug("Request to save SpendingTransaction : {}", spendingTransactionDTO);
        SpendingTransaction spendingTransaction = spendingTransactionMapper.spendingTransactionDTOToSpendingTransaction(spendingTransactionDTO);
        spendingTransaction = spendingTransactionRepository.save(spendingTransaction);
        SpendingTransactionDTO result = spendingTransactionMapper.spendingTransactionToSpendingTransactionDTO(spendingTransaction);
        spendingTransactionSearchRepository.save(spendingTransaction);
        return result;
    }

    /**
     *  Get all the spendingTransactions.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SpendingTransactionDTO> findAll() {
        log.debug("Request to get all SpendingTransactions");
        List<SpendingTransactionDTO> result = spendingTransactionRepository.findAll().stream()
            .map(spendingTransactionMapper::spendingTransactionToSpendingTransactionDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one spendingTransaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SpendingTransactionDTO findOne(Long id) {
        log.debug("Request to get SpendingTransaction : {}", id);
        SpendingTransaction spendingTransaction = spendingTransactionRepository.findOne(id);
        SpendingTransactionDTO spendingTransactionDTO = spendingTransactionMapper.spendingTransactionToSpendingTransactionDTO(spendingTransaction);
        return spendingTransactionDTO;
    }

    /**
     *  Delete the  spendingTransaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SpendingTransaction : {}", id);
        spendingTransactionRepository.delete(id);
        spendingTransactionSearchRepository.delete(id);
    }

    /**
     * Search for the spendingTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SpendingTransactionDTO> search(String query) {
        log.debug("Request to search SpendingTransactions for query {}", query);
        return StreamSupport
            .stream(spendingTransactionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(spendingTransactionMapper::spendingTransactionToSpendingTransactionDTO)
            .collect(Collectors.toList());
    }
}
