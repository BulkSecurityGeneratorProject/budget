package com.budget.service.impl;

import com.budget.service.AllyTransactionService;
import com.budget.domain.AllyTransaction;
import com.budget.domain.UploadedFiles;
import com.budget.domain.enumeration.AllyAccountType;
import com.budget.domain.enumeration.AllyTransactionType;
import com.budget.repository.AllyTransactionRepository;
import com.budget.repository.search.AllyTransactionSearchRepository;
import com.budget.service.dto.AllyTransactionDTO;
import com.budget.service.mapper.AllyTransactionMapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AllyTransaction.
 */
@Service
@Transactional
public class AllyTransactionServiceImpl implements AllyTransactionService{

    private static final int TYPE_INDEX = 3;

	private static final int AMOUNT_INDEX = 2;

	private static final int TIME_INDEX = 1;

	private static final int DATE_INDEX = 0;

	private final Logger log = LoggerFactory.getLogger(AllyTransactionServiceImpl.class);
    
    @Inject
    private AllyTransactionRepository allyTransactionRepository;

    @Inject
    private AllyTransactionMapper allyTransactionMapper;

    @Inject
    private AllyTransactionSearchRepository allyTransactionSearchRepository;

    /**
     * Save a allyTransaction.
     *
     * @param allyTransactionDTO the entity to save
     * @return the persisted entity
     */
    public AllyTransactionDTO save(AllyTransactionDTO allyTransactionDTO) {
        log.debug("Request to save AllyTransaction : {}", allyTransactionDTO);
        AllyTransaction allyTransaction = allyTransactionMapper.allyTransactionDTOToAllyTransaction(allyTransactionDTO);
        allyTransaction = allyTransactionRepository.save(allyTransaction);
        AllyTransactionDTO result = allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction);
        allyTransactionSearchRepository.save(allyTransaction);
        return result;
    }
    
    @Override
    public void parseCsvAndSave(UploadedFiles uploadedFile) {
    	CSVParser parser;
    	List<AllyTransaction> transactions = new ArrayList<>();
		try {
			parser = CSVFormat.newFormat(',').parse(
				    new InputStreamReader(new ByteArrayInputStream(uploadedFile.getFile()), "UTF8"));
	
			boolean firstRowComplete = false;
    		for (CSVRecord record : parser) {
    			if (firstRowComplete) {
    				AllyTransaction transaction = new AllyTransaction();
	    		  try {
	    			  transaction.setDate(getDateTime(record.get(DATE_INDEX), record.get(TIME_INDEX)));
	    			  transaction.setAmount(new BigDecimal(record.get(AMOUNT_INDEX)));
	    			  if (record.get(TYPE_INDEX).equals("Withdrawal")) {
	    				  transaction.setTransactionType(AllyTransactionType.WITHDRAWAL);
	    			  } else {
	    				  transaction.setTransactionType(AllyTransactionType.DEPOSIT);
	    			  }
	    			  transaction.setDescription(record.get(4));
	    			  transaction.setAccountType(uploadedFile.getAccountType());
	    		
	    		  } catch (Exception e) {
	    		    throw new RuntimeException("Error at line "
	    		      + parser.getCurrentLineNumber(), e);
	    		  }
	    		  transactions.add(transaction);
    			} else {
    				firstRowComplete = true;
    			}
    		}
    		parser.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		allyTransactionRepository.save(transactions);
    }
    
    private ZonedDateTime getDateTime(String date, String time) {
    	String dateTime = date + "T" + time+"Z";
    	Instant instant = Instant.parse(dateTime);
        return instant.atZone(ZoneId.of("America/New_York"));

    }

    /**
     *  Get all the allyTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AllyTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AllyTransactions");
        Page<AllyTransaction> result = allyTransactionRepository.findAll(pageable);
        return result.map(allyTransaction -> allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction));
    }

    /**
     *  Get one allyTransaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AllyTransactionDTO findOne(Long id) {
        log.debug("Request to get AllyTransaction : {}", id);
        AllyTransaction allyTransaction = allyTransactionRepository.findOne(id);
        AllyTransactionDTO allyTransactionDTO = allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction);
        return allyTransactionDTO;
    }

    /**
     *  Delete the  allyTransaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AllyTransaction : {}", id);
        allyTransactionRepository.delete(id);
        allyTransactionSearchRepository.delete(id);
    }

    /**
     * Search for the allyTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AllyTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AllyTransactions for query {}", query);
        Page<AllyTransaction> result = allyTransactionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(allyTransaction -> allyTransactionMapper.allyTransactionToAllyTransactionDTO(allyTransaction));
    }
}
