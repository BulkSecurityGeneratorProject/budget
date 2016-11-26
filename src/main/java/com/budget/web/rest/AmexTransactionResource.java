package com.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.budget.service.AmexTransactionService;
import com.budget.web.rest.util.HeaderUtil;
import com.budget.web.rest.util.PaginationUtil;
import com.budget.service.dto.AmexTransactionDTO;
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
 * REST controller for managing AmexTransaction.
 */
@RestController
@RequestMapping("/api")
public class AmexTransactionResource {

    private final Logger log = LoggerFactory.getLogger(AmexTransactionResource.class);
        
    @Inject
    private AmexTransactionService amexTransactionService;

    /**
     * POST  /amex-transactions : Create a new amexTransaction.
     *
     * @param amexTransactionDTO the amexTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new amexTransactionDTO, or with status 400 (Bad Request) if the amexTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/amex-transactions")
    @Timed
    public ResponseEntity<AmexTransactionDTO> createAmexTransaction(@Valid @RequestBody AmexTransactionDTO amexTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save AmexTransaction : {}", amexTransactionDTO);
        if (amexTransactionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("amexTransaction", "idexists", "A new amexTransaction cannot already have an ID")).body(null);
        }
        AmexTransactionDTO result = amexTransactionService.save(amexTransactionDTO);
        return ResponseEntity.created(new URI("/api/amex-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("amexTransaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /amex-transactions : Updates an existing amexTransaction.
     *
     * @param amexTransactionDTO the amexTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated amexTransactionDTO,
     * or with status 400 (Bad Request) if the amexTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the amexTransactionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/amex-transactions")
    @Timed
    public ResponseEntity<AmexTransactionDTO> updateAmexTransaction(@Valid @RequestBody AmexTransactionDTO amexTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update AmexTransaction : {}", amexTransactionDTO);
        if (amexTransactionDTO.getId() == null) {
            return createAmexTransaction(amexTransactionDTO);
        }
        AmexTransactionDTO result = amexTransactionService.save(amexTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("amexTransaction", amexTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /amex-transactions : get all the amexTransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of amexTransactions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/amex-transactions")
    @Timed
    public ResponseEntity<List<AmexTransactionDTO>> getAllAmexTransactions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AmexTransactions");
        Page<AmexTransactionDTO> page = amexTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/amex-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /amex-transactions/:id : get the "id" amexTransaction.
     *
     * @param id the id of the amexTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amexTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/amex-transactions/{id}")
    @Timed
    public ResponseEntity<AmexTransactionDTO> getAmexTransaction(@PathVariable Long id) {
        log.debug("REST request to get AmexTransaction : {}", id);
        AmexTransactionDTO amexTransactionDTO = amexTransactionService.findOne(id);
        return Optional.ofNullable(amexTransactionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /amex-transactions/:id : delete the "id" amexTransaction.
     *
     * @param id the id of the amexTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/amex-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAmexTransaction(@PathVariable Long id) {
        log.debug("REST request to delete AmexTransaction : {}", id);
        amexTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("amexTransaction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/amex-transactions?query=:query : search for the amexTransaction corresponding
     * to the query.
     *
     * @param query the query of the amexTransaction search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/amex-transactions")
    @Timed
    public ResponseEntity<List<AmexTransactionDTO>> searchAmexTransactions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AmexTransactions for query {}", query);
        Page<AmexTransactionDTO> page = amexTransactionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/amex-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
