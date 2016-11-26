package com.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.budget.service.AllyTransactionService;
import com.budget.web.rest.util.HeaderUtil;
import com.budget.web.rest.util.PaginationUtil;
import com.budget.service.dto.AllyTransactionDTO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AllyTransaction.
 */
@RestController
@RequestMapping("/api")
public class AllyTransactionResource {

    private final Logger log = LoggerFactory.getLogger(AllyTransactionResource.class);
        
    @Inject
    private AllyTransactionService allyTransactionService;

    /**
     * POST  /ally-transactions : Create a new allyTransaction.
     *
     * @param allyTransactionDTO the allyTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new allyTransactionDTO, or with status 400 (Bad Request) if the allyTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ally-transactions")
    @Timed
    public ResponseEntity<AllyTransactionDTO> createAllyTransaction(@Valid @RequestBody AllyTransactionDTO allyTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save AllyTransaction : {}", allyTransactionDTO);
        if (allyTransactionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("allyTransaction", "idexists", "A new allyTransaction cannot already have an ID")).body(null);
        }
        AllyTransactionDTO result = allyTransactionService.save(allyTransactionDTO);
        return ResponseEntity.created(new URI("/api/ally-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("allyTransaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ally-transactions : Updates an existing allyTransaction.
     *
     * @param allyTransactionDTO the allyTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated allyTransactionDTO,
     * or with status 400 (Bad Request) if the allyTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the allyTransactionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ally-transactions")
    @Timed
    public ResponseEntity<AllyTransactionDTO> updateAllyTransaction(@Valid @RequestBody AllyTransactionDTO allyTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update AllyTransaction : {}", allyTransactionDTO);
        if (allyTransactionDTO.getId() == null) {
            return createAllyTransaction(allyTransactionDTO);
        }
        AllyTransactionDTO result = allyTransactionService.save(allyTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("allyTransaction", allyTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ally-transactions : get all the allyTransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of allyTransactions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/ally-transactions")
    @Timed
    public ResponseEntity<List<AllyTransactionDTO>> getAllAllyTransactions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AllyTransactions");
        Page<AllyTransactionDTO> page = allyTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ally-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ally-transactions/:id : get the "id" allyTransaction.
     *
     * @param id the id of the allyTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the allyTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ally-transactions/{id}")
    @Timed
    public ResponseEntity<AllyTransactionDTO> getAllyTransaction(@PathVariable Long id) {
        log.debug("REST request to get AllyTransaction : {}", id);
        AllyTransactionDTO allyTransactionDTO = allyTransactionService.findOne(id);
        return Optional.ofNullable(allyTransactionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ally-transactions/:id : delete the "id" allyTransaction.
     *
     * @param id the id of the allyTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ally-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAllyTransaction(@PathVariable Long id) {
        log.debug("REST request to delete AllyTransaction : {}", id);
        allyTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("allyTransaction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ally-transactions?query=:query : search for the allyTransaction corresponding
     * to the query.
     *
     * @param query the query of the allyTransaction search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/ally-transactions")
    @Timed
    public ResponseEntity<List<AllyTransactionDTO>> searchAllyTransactions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AllyTransactions for query {}", query);
        Page<AllyTransactionDTO> page = allyTransactionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ally-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
