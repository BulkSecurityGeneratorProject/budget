package com.budget.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.budget.service.dto.AllyTransactionDTO;
import com.budget.service.dto.UploadDTO;
import com.budget.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class UploadTransactionResource {
	
    private final Logger log = LoggerFactory.getLogger(UploadTransactionResource.class);

    @RequestMapping(value = "/upload-transactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
    public void uploadTransactions(@Valid @RequestBody UploadDTO uploadDTO) throws URISyntaxException {
        log.debug("REST request to uploadTransaction : {}", uploadDTO);
        
        System.out.println("KEVIN");

//        AllyTransactionDTO result = allyTransactionService.save(allyTransactionDTO);
//        return ResponseEntity.created(new URI("/api/ally-transactions/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert("allyTransaction", result.getId().toString()))
//            .body(result);
    }

}
