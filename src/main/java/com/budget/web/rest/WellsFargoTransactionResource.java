package com.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.budget.domain.WellsFargoTransaction;
import com.budget.service.WellsFargoTransactionService;
import com.budget.web.rest.util.HeaderUtil;
import com.budget.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing WellsFargoTransaction.
 */
@RestController
@RequestMapping("/api")
public class WellsFargoTransactionResource {

    private final Logger log = LoggerFactory.getLogger(WellsFargoTransactionResource.class);
        
    @Inject
    private WellsFargoTransactionService wellsFargoTransactionService;

    /**
     * POST  /wells-fargo-transactions : Create a new wellsFargoTransaction.
     *
     * @param wellsFargoTransaction the wellsFargoTransaction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wellsFargoTransaction, or with status 400 (Bad Request) if the wellsFargoTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wells-fargo-transactions")
    @Timed
    public ResponseEntity<WellsFargoTransaction> createWellsFargoTransaction(@Valid @RequestBody WellsFargoTransaction wellsFargoTransaction) throws URISyntaxException {
        log.debug("REST request to save WellsFargoTransaction : {}", wellsFargoTransaction);
        if (wellsFargoTransaction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("wellsFargoTransaction", "idexists", "A new wellsFargoTransaction cannot already have an ID")).body(null);
        }
        WellsFargoTransaction result = wellsFargoTransactionService.save(wellsFargoTransaction);
        return ResponseEntity.created(new URI("/api/wells-fargo-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("wellsFargoTransaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wells-fargo-transactions : Updates an existing wellsFargoTransaction.
     *
     * @param wellsFargoTransaction the wellsFargoTransaction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wellsFargoTransaction,
     * or with status 400 (Bad Request) if the wellsFargoTransaction is not valid,
     * or with status 500 (Internal Server Error) if the wellsFargoTransaction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wells-fargo-transactions")
    @Timed
    public ResponseEntity<WellsFargoTransaction> updateWellsFargoTransaction(@Valid @RequestBody WellsFargoTransaction wellsFargoTransaction) throws URISyntaxException {
        log.debug("REST request to update WellsFargoTransaction : {}", wellsFargoTransaction);
        if (wellsFargoTransaction.getId() == null) {
            return createWellsFargoTransaction(wellsFargoTransaction);
        }
        WellsFargoTransaction result = wellsFargoTransactionService.save(wellsFargoTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("wellsFargoTransaction", wellsFargoTransaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wells-fargo-transactions : get all the wellsFargoTransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wellsFargoTransactions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/wells-fargo-transactions")
    @Timed
    public ResponseEntity<List<WellsFargoTransaction>> getAllWellsFargoTransactions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WellsFargoTransactions");
        Page<WellsFargoTransaction> page = wellsFargoTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wells-fargo-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wells-fargo-transactions/:id : get the "id" wellsFargoTransaction.
     *
     * @param id the id of the wellsFargoTransaction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wellsFargoTransaction, or with status 404 (Not Found)
     */
    @GetMapping("/wells-fargo-transactions/{id}")
    @Timed
    public ResponseEntity<WellsFargoTransaction> getWellsFargoTransaction(@PathVariable Long id) {
        log.debug("REST request to get WellsFargoTransaction : {}", id);
        WellsFargoTransaction wellsFargoTransaction = wellsFargoTransactionService.findOne(id);
        return Optional.ofNullable(wellsFargoTransaction)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /wells-fargo-transactions/:id : delete the "id" wellsFargoTransaction.
     *
     * @param id the id of the wellsFargoTransaction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wells-fargo-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteWellsFargoTransaction(@PathVariable Long id) {
        log.debug("REST request to delete WellsFargoTransaction : {}", id);
        wellsFargoTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("wellsFargoTransaction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/wells-fargo-transactions?query=:query : search for the wellsFargoTransaction corresponding
     * to the query.
     *
     * @param query the query of the wellsFargoTransaction search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/wells-fargo-transactions")
    @Timed
    public ResponseEntity<List<WellsFargoTransaction>> searchWellsFargoTransactions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WellsFargoTransactions for query {}", query);
        Page<WellsFargoTransaction> page = wellsFargoTransactionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/wells-fargo-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
