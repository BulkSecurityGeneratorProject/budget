package com.budget.service;

import com.budget.service.dto.BillTransactionDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing BillTransaction.
 */
public interface BillTransactionService {

    /**
     * Save a billTransaction.
     *
     * @param billTransactionDTO the entity to save
     * @return the persisted entity
     */
    BillTransactionDTO save(BillTransactionDTO billTransactionDTO);

    /**
     *  Get all the billTransactions.
     *  
     *  @return the list of entities
     */
    List<BillTransactionDTO> findAll();

    /**
     *  Get the "id" billTransaction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BillTransactionDTO findOne(Long id);

    /**
     *  Delete the "id" billTransaction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the billTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<BillTransactionDTO> search(String query);
}
