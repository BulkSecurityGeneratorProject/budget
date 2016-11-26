package com.budget.service;

import com.budget.service.dto.SinkingFundTransactionDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing SinkingFundTransaction.
 */
public interface SinkingFundTransactionService {

    /**
     * Save a sinkingFundTransaction.
     *
     * @param sinkingFundTransactionDTO the entity to save
     * @return the persisted entity
     */
    SinkingFundTransactionDTO save(SinkingFundTransactionDTO sinkingFundTransactionDTO);

    /**
     *  Get all the sinkingFundTransactions.
     *  
     *  @return the list of entities
     */
    List<SinkingFundTransactionDTO> findAll();

    /**
     *  Get the "id" sinkingFundTransaction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SinkingFundTransactionDTO findOne(Long id);

    /**
     *  Delete the "id" sinkingFundTransaction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sinkingFundTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<SinkingFundTransactionDTO> search(String query);
}
