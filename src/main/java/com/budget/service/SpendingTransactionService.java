package com.budget.service;

import com.budget.service.dto.SpendingTransactionDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing SpendingTransaction.
 */
public interface SpendingTransactionService {

    /**
     * Save a spendingTransaction.
     *
     * @param spendingTransactionDTO the entity to save
     * @return the persisted entity
     */
    SpendingTransactionDTO save(SpendingTransactionDTO spendingTransactionDTO);

    /**
     *  Get all the spendingTransactions.
     *  
     *  @return the list of entities
     */
    List<SpendingTransactionDTO> findAll();

    /**
     *  Get the "id" spendingTransaction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SpendingTransactionDTO findOne(Long id);

    /**
     *  Delete the "id" spendingTransaction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the spendingTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<SpendingTransactionDTO> search(String query);
}
