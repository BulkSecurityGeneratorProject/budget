package com.budget.service.impl;

import com.budget.service.BillTransactionService;
import com.budget.domain.BillTransaction;
import com.budget.repository.BillTransactionRepository;
import com.budget.repository.search.BillTransactionSearchRepository;
import com.budget.service.dto.BillTransactionDTO;
import com.budget.service.mapper.BillTransactionMapper;
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
 * Service Implementation for managing BillTransaction.
 */
@Service
@Transactional
public class BillTransactionServiceImpl implements BillTransactionService{

    private final Logger log = LoggerFactory.getLogger(BillTransactionServiceImpl.class);
    
    @Inject
    private BillTransactionRepository billTransactionRepository;

    @Inject
    private BillTransactionMapper billTransactionMapper;

    @Inject
    private BillTransactionSearchRepository billTransactionSearchRepository;

    /**
     * Save a billTransaction.
     *
     * @param billTransactionDTO the entity to save
     * @return the persisted entity
     */
    public BillTransactionDTO save(BillTransactionDTO billTransactionDTO) {
        log.debug("Request to save BillTransaction : {}", billTransactionDTO);
        BillTransaction billTransaction = billTransactionMapper.billTransactionDTOToBillTransaction(billTransactionDTO);
        billTransaction = billTransactionRepository.save(billTransaction);
        BillTransactionDTO result = billTransactionMapper.billTransactionToBillTransactionDTO(billTransaction);
        billTransactionSearchRepository.save(billTransaction);
        return result;
    }

    /**
     *  Get all the billTransactions.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<BillTransactionDTO> findAll() {
        log.debug("Request to get all BillTransactions");
        List<BillTransactionDTO> result = billTransactionRepository.findAll().stream()
            .map(billTransactionMapper::billTransactionToBillTransactionDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one billTransaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BillTransactionDTO findOne(Long id) {
        log.debug("Request to get BillTransaction : {}", id);
        BillTransaction billTransaction = billTransactionRepository.findOne(id);
        BillTransactionDTO billTransactionDTO = billTransactionMapper.billTransactionToBillTransactionDTO(billTransaction);
        return billTransactionDTO;
    }

    /**
     *  Delete the  billTransaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BillTransaction : {}", id);
        billTransactionRepository.delete(id);
        billTransactionSearchRepository.delete(id);
    }

    /**
     * Search for the billTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BillTransactionDTO> search(String query) {
        log.debug("Request to search BillTransactions for query {}", query);
        return StreamSupport
            .stream(billTransactionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(billTransactionMapper::billTransactionToBillTransactionDTO)
            .collect(Collectors.toList());
    }
}
