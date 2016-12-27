package com.budget.service;

import com.budget.domain.WellsFargoTransaction;
import com.budget.repository.WellsFargoTransactionRepository;
import com.budget.repository.search.WellsFargoTransactionSearchRepository;
import com.budget.service.common.BaseTransactionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Interface for managing WellsFargoTransactionService.
 */
public interface WellsFargoTransactionService extends BaseTransactionService {


    /**
     * Save a wellsFargoTransaction.
     *
     * @param wellsFargoTransaction the entity to save
     * @return the persisted entity
     */
   WellsFargoTransaction save(WellsFargoTransaction wellsFargoTransaction);

    /**
     *  Get all the wellsFargoTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WellsFargoTransaction> findAll(Pageable pageable);

    /**
     *  Get one wellsFargoTransaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WellsFargoTransaction findOne(Long id);

    /**
     *  Delete the  wellsFargoTransaction by id.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the wellsFargoTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<WellsFargoTransaction> search(String query, Pageable pageable);
}
