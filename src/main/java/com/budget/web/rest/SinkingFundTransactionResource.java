package com.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.budget.service.SinkingFundTransactionService;
import com.budget.web.rest.util.HeaderUtil;
import com.budget.service.dto.SinkingFundTransactionDTO;
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
 * REST controller for managing SinkingFundTransaction.
 */
@RestController
@RequestMapping("/api")
public class SinkingFundTransactionResource {

    private final Logger log = LoggerFactory.getLogger(SinkingFundTransactionResource.class);
        
    @Inject
    private SinkingFundTransactionService sinkingFundTransactionService;

    /**
     * POST  /sinking-fund-transactions : Create a new sinkingFundTransaction.
     *
     * @param sinkingFundTransactionDTO the sinkingFundTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sinkingFundTransactionDTO, or with status 400 (Bad Request) if the sinkingFundTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sinking-fund-transactions")
    @Timed
    public ResponseEntity<SinkingFundTransactionDTO> createSinkingFundTransaction(@Valid @RequestBody SinkingFundTransactionDTO sinkingFundTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save SinkingFundTransaction : {}", sinkingFundTransactionDTO);
        if (sinkingFundTransactionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sinkingFundTransaction", "idexists", "A new sinkingFundTransaction cannot already have an ID")).body(null);
        }
        SinkingFundTransactionDTO result = sinkingFundTransactionService.save(sinkingFundTransactionDTO);
        return ResponseEntity.created(new URI("/api/sinking-fund-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sinkingFundTransaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sinking-fund-transactions : Updates an existing sinkingFundTransaction.
     *
     * @param sinkingFundTransactionDTO the sinkingFundTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sinkingFundTransactionDTO,
     * or with status 400 (Bad Request) if the sinkingFundTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the sinkingFundTransactionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sinking-fund-transactions")
    @Timed
    public ResponseEntity<SinkingFundTransactionDTO> updateSinkingFundTransaction(@Valid @RequestBody SinkingFundTransactionDTO sinkingFundTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update SinkingFundTransaction : {}", sinkingFundTransactionDTO);
        if (sinkingFundTransactionDTO.getId() == null) {
            return createSinkingFundTransaction(sinkingFundTransactionDTO);
        }
        SinkingFundTransactionDTO result = sinkingFundTransactionService.save(sinkingFundTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sinkingFundTransaction", sinkingFundTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sinking-fund-transactions : get all the sinkingFundTransactions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sinkingFundTransactions in body
     */
    @GetMapping("/sinking-fund-transactions")
    @Timed
    public List<SinkingFundTransactionDTO> getAllSinkingFundTransactions() {
        log.debug("REST request to get all SinkingFundTransactions");
        return sinkingFundTransactionService.findAll();
    }

    /**
     * GET  /sinking-fund-transactions/:id : get the "id" sinkingFundTransaction.
     *
     * @param id the id of the sinkingFundTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sinkingFundTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sinking-fund-transactions/{id}")
    @Timed
    public ResponseEntity<SinkingFundTransactionDTO> getSinkingFundTransaction(@PathVariable Long id) {
        log.debug("REST request to get SinkingFundTransaction : {}", id);
        SinkingFundTransactionDTO sinkingFundTransactionDTO = sinkingFundTransactionService.findOne(id);
        return Optional.ofNullable(sinkingFundTransactionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sinking-fund-transactions/:id : delete the "id" sinkingFundTransaction.
     *
     * @param id the id of the sinkingFundTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sinking-fund-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteSinkingFundTransaction(@PathVariable Long id) {
        log.debug("REST request to delete SinkingFundTransaction : {}", id);
        sinkingFundTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sinkingFundTransaction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/sinking-fund-transactions?query=:query : search for the sinkingFundTransaction corresponding
     * to the query.
     *
     * @param query the query of the sinkingFundTransaction search 
     * @return the result of the search
     */
    @GetMapping("/_search/sinking-fund-transactions")
    @Timed
    public List<SinkingFundTransactionDTO> searchSinkingFundTransactions(@RequestParam String query) {
        log.debug("REST request to search SinkingFundTransactions for query {}", query);
        return sinkingFundTransactionService.search(query);
    }


}
