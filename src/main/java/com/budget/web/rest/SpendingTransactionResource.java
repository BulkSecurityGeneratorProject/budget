package com.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.budget.service.SpendingTransactionService;
import com.budget.web.rest.util.HeaderUtil;
import com.budget.service.dto.SpendingTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SpendingTransaction.
 */
@RestController
@RequestMapping("/api")
public class SpendingTransactionResource {

    private final Logger log = LoggerFactory.getLogger(SpendingTransactionResource.class);
        
    @Inject
    private SpendingTransactionService spendingTransactionService;

    /**
     * POST  /spending-transactions : Create a new spendingTransaction.
     *
     * @param spendingTransactionDTO the spendingTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new spendingTransactionDTO, or with status 400 (Bad Request) if the spendingTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/spending-transactions")
    @Timed
    public ResponseEntity<SpendingTransactionDTO> createSpendingTransaction(@Valid @RequestBody SpendingTransactionDTO spendingTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save SpendingTransaction : {}", spendingTransactionDTO);
        if (spendingTransactionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("spendingTransaction", "idexists", "A new spendingTransaction cannot already have an ID")).body(null);
        }
        SpendingTransactionDTO result = spendingTransactionService.save(spendingTransactionDTO);
        return ResponseEntity.created(new URI("/api/spending-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("spendingTransaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /spending-transactions : Updates an existing spendingTransaction.
     *
     * @param spendingTransactionDTO the spendingTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated spendingTransactionDTO,
     * or with status 400 (Bad Request) if the spendingTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the spendingTransactionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/spending-transactions")
    @Timed
    public ResponseEntity<SpendingTransactionDTO> updateSpendingTransaction(@Valid @RequestBody SpendingTransactionDTO spendingTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update SpendingTransaction : {}", spendingTransactionDTO);
        if (spendingTransactionDTO.getId() == null) {
            return createSpendingTransaction(spendingTransactionDTO);
        }
        SpendingTransactionDTO result = spendingTransactionService.save(spendingTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("spendingTransaction", spendingTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /spending-transactions : get all the spendingTransactions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of spendingTransactions in body
     */
    @GetMapping("/spending-transactions")
    @Timed
    public List<SpendingTransactionDTO> getAllSpendingTransactions() {
        log.debug("REST request to get all SpendingTransactions");
        return spendingTransactionService.findAll();
    }

    /**
     * GET  /spending-transactions/:id : get the "id" spendingTransaction.
     *
     * @param id the id of the spendingTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the spendingTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/spending-transactions/{id}")
    @Timed
    public ResponseEntity<SpendingTransactionDTO> getSpendingTransaction(@PathVariable Long id) {
        log.debug("REST request to get SpendingTransaction : {}", id);
        SpendingTransactionDTO spendingTransactionDTO = spendingTransactionService.findOne(id);
        return Optional.ofNullable(spendingTransactionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /spending-transactions/:id : delete the "id" spendingTransaction.
     *
     * @param id the id of the spendingTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/spending-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteSpendingTransaction(@PathVariable Long id) {
        log.debug("REST request to delete SpendingTransaction : {}", id);
        spendingTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("spendingTransaction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/spending-transactions?query=:query : search for the spendingTransaction corresponding
     * to the query.
     *
     * @param query the query of the spendingTransaction search 
     * @return the result of the search
     */
    @GetMapping("/_search/spending-transactions")
    @Timed
    public List<SpendingTransactionDTO> searchSpendingTransactions(@RequestParam String query) {
        log.debug("REST request to search SpendingTransactions for query {}", query);
        return spendingTransactionService.search(query);
    }


}
