package com.budget.service;

import com.budget.service.common.BaseTransactionService;
import com.budget.service.dto.AllyTransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Service Interface for managing AllyTransaction.
 */
public interface AllyTransactionService extends BaseTransactionService {

    /**
     * Save a allyTransaction.
     *
     * @param allyTransactionDTO the entity to save
     * @return the persisted entity
     */
    AllyTransactionDTO save(AllyTransactionDTO allyTransactionDTO);

    /**
     *  Get all the allyTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AllyTransactionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" allyTransaction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AllyTransactionDTO findOne(Long id);

    /**
     *  Delete the "id" allyTransaction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the allyTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AllyTransactionDTO> search(String query, Pageable pageable);

}
