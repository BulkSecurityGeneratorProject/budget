package com.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.budget.service.BillTransactionService;
import com.budget.web.rest.util.HeaderUtil;
import com.budget.service.dto.BillTransactionDTO;
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
 * REST controller for managing BillTransaction.
 */
@RestController
@RequestMapping("/api")
public class BillTransactionResource {

    private final Logger log = LoggerFactory.getLogger(BillTransactionResource.class);
        
    @Inject
    private BillTransactionService billTransactionService;

    /**
     * POST  /bill-transactions : Create a new billTransaction.
     *
     * @param billTransactionDTO the billTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billTransactionDTO, or with status 400 (Bad Request) if the billTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bill-transactions")
    @Timed
    public ResponseEntity<BillTransactionDTO> createBillTransaction(@Valid @RequestBody BillTransactionDTO billTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save BillTransaction : {}", billTransactionDTO);
        if (billTransactionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("billTransaction", "idexists", "A new billTransaction cannot already have an ID")).body(null);
        }
        BillTransactionDTO result = billTransactionService.save(billTransactionDTO);
        return ResponseEntity.created(new URI("/api/bill-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("billTransaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bill-transactions : Updates an existing billTransaction.
     *
     * @param billTransactionDTO the billTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billTransactionDTO,
     * or with status 400 (Bad Request) if the billTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the billTransactionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bill-transactions")
    @Timed
    public ResponseEntity<BillTransactionDTO> updateBillTransaction(@Valid @RequestBody BillTransactionDTO billTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update BillTransaction : {}", billTransactionDTO);
        if (billTransactionDTO.getId() == null) {
            return createBillTransaction(billTransactionDTO);
        }
        BillTransactionDTO result = billTransactionService.save(billTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("billTransaction", billTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bill-transactions : get all the billTransactions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of billTransactions in body
     */
    @GetMapping("/bill-transactions")
    @Timed
    public List<BillTransactionDTO> getAllBillTransactions() {
        log.debug("REST request to get all BillTransactions");
        return billTransactionService.findAll();
    }

    /**
     * GET  /bill-transactions/:id : get the "id" billTransaction.
     *
     * @param id the id of the billTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bill-transactions/{id}")
    @Timed
    public ResponseEntity<BillTransactionDTO> getBillTransaction(@PathVariable Long id) {
        log.debug("REST request to get BillTransaction : {}", id);
        BillTransactionDTO billTransactionDTO = billTransactionService.findOne(id);
        return Optional.ofNullable(billTransactionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bill-transactions/:id : delete the "id" billTransaction.
     *
     * @param id the id of the billTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bill-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteBillTransaction(@PathVariable Long id) {
        log.debug("REST request to delete BillTransaction : {}", id);
        billTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("billTransaction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bill-transactions?query=:query : search for the billTransaction corresponding
     * to the query.
     *
     * @param query the query of the billTransaction search 
     * @return the result of the search
     */
    @GetMapping("/_search/bill-transactions")
    @Timed
    public List<BillTransactionDTO> searchBillTransactions(@RequestParam String query) {
        log.debug("REST request to search BillTransactions for query {}", query);
        return billTransactionService.search(query);
    }


}
