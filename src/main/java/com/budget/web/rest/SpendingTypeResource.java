package com.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.budget.domain.SpendingType;

import com.budget.repository.SpendingTypeRepository;
import com.budget.repository.search.SpendingTypeSearchRepository;
import com.budget.web.rest.util.HeaderUtil;
import com.budget.service.dto.SpendingTypeDTO;
import com.budget.service.mapper.SpendingTypeMapper;
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
 * REST controller for managing SpendingType.
 */
@RestController
@RequestMapping("/api")
public class SpendingTypeResource {

    private final Logger log = LoggerFactory.getLogger(SpendingTypeResource.class);
        
    @Inject
    private SpendingTypeRepository spendingTypeRepository;

    @Inject
    private SpendingTypeMapper spendingTypeMapper;

    @Inject
    private SpendingTypeSearchRepository spendingTypeSearchRepository;

    /**
     * POST  /spending-types : Create a new spendingType.
     *
     * @param spendingTypeDTO the spendingTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new spendingTypeDTO, or with status 400 (Bad Request) if the spendingType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/spending-types")
    @Timed
    public ResponseEntity<SpendingTypeDTO> createSpendingType(@RequestBody SpendingTypeDTO spendingTypeDTO) throws URISyntaxException {
        log.debug("REST request to save SpendingType : {}", spendingTypeDTO);
        if (spendingTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("spendingType", "idexists", "A new spendingType cannot already have an ID")).body(null);
        }
        SpendingType spendingType = spendingTypeMapper.spendingTypeDTOToSpendingType(spendingTypeDTO);
        spendingType = spendingTypeRepository.save(spendingType);
        SpendingTypeDTO result = spendingTypeMapper.spendingTypeToSpendingTypeDTO(spendingType);
        spendingTypeSearchRepository.save(spendingType);
        return ResponseEntity.created(new URI("/api/spending-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("spendingType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /spending-types : Updates an existing spendingType.
     *
     * @param spendingTypeDTO the spendingTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated spendingTypeDTO,
     * or with status 400 (Bad Request) if the spendingTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the spendingTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/spending-types")
    @Timed
    public ResponseEntity<SpendingTypeDTO> updateSpendingType(@RequestBody SpendingTypeDTO spendingTypeDTO) throws URISyntaxException {
        log.debug("REST request to update SpendingType : {}", spendingTypeDTO);
        if (spendingTypeDTO.getId() == null) {
            return createSpendingType(spendingTypeDTO);
        }
        SpendingType spendingType = spendingTypeMapper.spendingTypeDTOToSpendingType(spendingTypeDTO);
        spendingType = spendingTypeRepository.save(spendingType);
        SpendingTypeDTO result = spendingTypeMapper.spendingTypeToSpendingTypeDTO(spendingType);
        spendingTypeSearchRepository.save(spendingType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("spendingType", spendingTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /spending-types : get all the spendingTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of spendingTypes in body
     */
    @GetMapping("/spending-types")
    @Timed
    public List<SpendingTypeDTO> getAllSpendingTypes() {
        log.debug("REST request to get all SpendingTypes");
        List<SpendingType> spendingTypes = spendingTypeRepository.findAll();
        return spendingTypeMapper.spendingTypesToSpendingTypeDTOs(spendingTypes);
    }

    /**
     * GET  /spending-types/:id : get the "id" spendingType.
     *
     * @param id the id of the spendingTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the spendingTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/spending-types/{id}")
    @Timed
    public ResponseEntity<SpendingTypeDTO> getSpendingType(@PathVariable Long id) {
        log.debug("REST request to get SpendingType : {}", id);
        SpendingType spendingType = spendingTypeRepository.findOne(id);
        SpendingTypeDTO spendingTypeDTO = spendingTypeMapper.spendingTypeToSpendingTypeDTO(spendingType);
        return Optional.ofNullable(spendingTypeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /spending-types/:id : delete the "id" spendingType.
     *
     * @param id the id of the spendingTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/spending-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteSpendingType(@PathVariable Long id) {
        log.debug("REST request to delete SpendingType : {}", id);
        spendingTypeRepository.delete(id);
        spendingTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("spendingType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/spending-types?query=:query : search for the spendingType corresponding
     * to the query.
     *
     * @param query the query of the spendingType search 
     * @return the result of the search
     */
    @GetMapping("/_search/spending-types")
    @Timed
    public List<SpendingTypeDTO> searchSpendingTypes(@RequestParam String query) {
        log.debug("REST request to search SpendingTypes for query {}", query);
        return StreamSupport
            .stream(spendingTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(spendingTypeMapper::spendingTypeToSpendingTypeDTO)
            .collect(Collectors.toList());
    }


}
