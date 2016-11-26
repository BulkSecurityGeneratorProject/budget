package com.budget.service;

import com.budget.service.dto.AmexTransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing AmexTransaction.
 */
public interface AmexTransactionService {

    /**
     * Save a amexTransaction.
     *
     * @param amexTransactionDTO the entity to save
     * @return the persisted entity
     */
    AmexTransactionDTO save(AmexTransactionDTO amexTransactionDTO);

    /**
     *  Get all the amexTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AmexTransactionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" amexTransaction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AmexTransactionDTO findOne(Long id);

    /**
     *  Delete the "id" amexTransaction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the amexTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AmexTransactionDTO> search(String query, Pageable pageable);
}
