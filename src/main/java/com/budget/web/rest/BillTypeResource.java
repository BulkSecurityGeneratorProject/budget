package com.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.budget.domain.BillType;

import com.budget.repository.BillTypeRepository;
import com.budget.repository.search.BillTypeSearchRepository;
import com.budget.web.rest.util.HeaderUtil;
import com.budget.service.dto.BillTypeDTO;
import com.budget.service.mapper.BillTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BillType.
 */
@RestController
@RequestMapping("/api")
public class BillTypeResource {

    private final Logger log = LoggerFactory.getLogger(BillTypeResource.class);
        
    @Inject
    private BillTypeRepository billTypeRepository;

    @Inject
    private BillTypeMapper billTypeMapper;

    @Inject
    private BillTypeSearchRepository billTypeSearchRepository;

    /**
     * POST  /bill-types : Create a new billType.
     *
     * @param billTypeDTO the billTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billTypeDTO, or with status 400 (Bad Request) if the billType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bill-types")
    @Timed
    public ResponseEntity<BillTypeDTO> createBillType(@RequestBody BillTypeDTO billTypeDTO) throws URISyntaxException {
        log.debug("REST request to save BillType : {}", billTypeDTO);
        if (billTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("billType", "idexists", "A new billType cannot already have an ID")).body(null);
        }
        BillType billType = billTypeMapper.billTypeDTOToBillType(billTypeDTO);
        billType = billTypeRepository.save(billType);
        BillTypeDTO result = billTypeMapper.billTypeToBillTypeDTO(billType);
        billTypeSearchRepository.save(billType);
        return ResponseEntity.created(new URI("/api/bill-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("billType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bill-types : Updates an existing billType.
     *
     * @param billTypeDTO the billTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billTypeDTO,
     * or with status 400 (Bad Request) if the billTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the billTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bill-types")
    @Timed
    public ResponseEntity<BillTypeDTO> updateBillType(@RequestBody BillTypeDTO billTypeDTO) throws URISyntaxException {
        log.debug("REST request to update BillType : {}", billTypeDTO);
        if (billTypeDTO.getId() == null) {
            return createBillType(billTypeDTO);
        }
        BillType billType = billTypeMapper.billTypeDTOToBillType(billTypeDTO);
        billType = billTypeRepository.save(billType);
        BillTypeDTO result = billTypeMapper.billTypeToBillTypeDTO(billType);
        billTypeSearchRepository.save(billType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("billType", billTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bill-types : get all the billTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of billTypes in body
     */
    @GetMapping("/bill-types")
    @Timed
    public List<BillTypeDTO> getAllBillTypes() {
        log.debug("REST request to get all BillTypes");
        List<BillType> billTypes = billTypeRepository.findAll();
        return billTypeMapper.billTypesToBillTypeDTOs(billTypes);
    }

    /**
     * GET  /bill-types/:id : get the "id" billType.
     *
     * @param id the id of the billTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bill-types/{id}")
    @Timed
    public ResponseEntity<BillTypeDTO> getBillType(@PathVariable Long id) {
        log.debug("REST request to get BillType : {}", id);
        BillType billType = billTypeRepository.findOne(id);
        BillTypeDTO billTypeDTO = billTypeMapper.billTypeToBillTypeDTO(billType);
        return Optional.ofNullable(billTypeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bill-types/:id : delete the "id" billType.
     *
     * @param id the id of the billTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bill-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteBillType(@PathVariable Long id) {
        log.debug("REST request to delete BillType : {}", id);
        billTypeRepository.delete(id);
        billTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("billType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bill-types?query=:query : search for the billType corresponding
     * to the query.
     *
     * @param query the query of the billType search 
     * @return the result of the search
     */
    @GetMapping("/_search/bill-types")
    @Timed
    public List<BillTypeDTO> searchBillTypes(@RequestParam String query) {
        log.debug("REST request to search BillTypes for query {}", query);
        return StreamSupport
            .stream(billTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(billTypeMapper::billTypeToBillTypeDTO)
            .collect(Collectors.toList());
    }


}
