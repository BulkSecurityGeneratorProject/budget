package com.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.budget.service.UploadedFilesService;
import com.budget.web.rest.util.HeaderUtil;
import com.budget.web.rest.util.PaginationUtil;
import com.budget.service.dto.UploadedFilesDTO;
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
 * REST controller for managing UploadedFiles.
 */
@RestController
@RequestMapping("/api")
public class UploadedFilesResource {

    private final Logger log = LoggerFactory.getLogger(UploadedFilesResource.class);
        
    @Inject
    private UploadedFilesService uploadedFilesService;

    /**
     * POST  /uploaded-files : Create a new uploadedFiles.
     *
     * @param uploadedFilesDTO the uploadedFilesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new uploadedFilesDTO, or with status 400 (Bad Request) if the uploadedFiles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/uploaded-files")
    @Timed
    public ResponseEntity<UploadedFilesDTO> createUploadedFiles(@Valid @RequestBody UploadedFilesDTO uploadedFilesDTO) throws URISyntaxException {
        log.debug("REST request to save UploadedFiles : {}", uploadedFilesDTO);
        if (uploadedFilesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("uploadedFiles", "idexists", "A new uploadedFiles cannot already have an ID")).body(null);
        }
        UploadedFilesDTO result = uploadedFilesService.save(uploadedFilesDTO);
        return ResponseEntity.created(new URI("/api/uploaded-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("uploadedFiles", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /uploaded-files : Updates an existing uploadedFiles.
     *
     * @param uploadedFilesDTO the uploadedFilesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated uploadedFilesDTO,
     * or with status 400 (Bad Request) if the uploadedFilesDTO is not valid,
     * or with status 500 (Internal Server Error) if the uploadedFilesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/uploaded-files")
    @Timed
    public ResponseEntity<UploadedFilesDTO> updateUploadedFiles(@Valid @RequestBody UploadedFilesDTO uploadedFilesDTO) throws URISyntaxException {
        log.debug("REST request to update UploadedFiles : {}", uploadedFilesDTO);
        if (uploadedFilesDTO.getId() == null) {
            return createUploadedFiles(uploadedFilesDTO);
        }
        UploadedFilesDTO result = uploadedFilesService.save(uploadedFilesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("uploadedFiles", uploadedFilesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /uploaded-files : get all the uploadedFiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of uploadedFiles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/uploaded-files")
    @Timed
    public ResponseEntity<List<UploadedFilesDTO>> getAllUploadedFiles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UploadedFiles");
        Page<UploadedFilesDTO> page = uploadedFilesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/uploaded-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /uploaded-files/:id : get the "id" uploadedFiles.
     *
     * @param id the id of the uploadedFilesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the uploadedFilesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/uploaded-files/{id}")
    @Timed
    public ResponseEntity<UploadedFilesDTO> getUploadedFiles(@PathVariable Long id) {
        log.debug("REST request to get UploadedFiles : {}", id);
        UploadedFilesDTO uploadedFilesDTO = uploadedFilesService.findOne(id);
        return Optional.ofNullable(uploadedFilesDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /uploaded-files/:id : delete the "id" uploadedFiles.
     *
     * @param id the id of the uploadedFilesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/uploaded-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteUploadedFiles(@PathVariable Long id) {
        log.debug("REST request to delete UploadedFiles : {}", id);
        uploadedFilesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("uploadedFiles", id.toString())).build();
    }

    /**
     * SEARCH  /_search/uploaded-files?query=:query : search for the uploadedFiles corresponding
     * to the query.
     *
     * @param query the query of the uploadedFiles search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/uploaded-files")
    @Timed
    public ResponseEntity<List<UploadedFilesDTO>> searchUploadedFiles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of UploadedFiles for query {}", query);
        Page<UploadedFilesDTO> page = uploadedFilesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/uploaded-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
