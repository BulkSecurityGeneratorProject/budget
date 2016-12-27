package com.budget.service.impl;

import com.budget.service.AmexTransactionService;
import com.budget.domain.AllyTransaction;
import com.budget.domain.AmexTransaction;
import com.budget.domain.UploadedFiles;
import com.budget.domain.enumeration.AllyTransactionType;
import com.budget.repository.AmexTransactionRepository;
import com.budget.repository.search.AmexTransactionSearchRepository;
import com.budget.service.dto.AmexTransactionDTO;
import com.budget.service.impl.common.AbstractBaseTransactionService;
import com.budget.service.mapper.AmexTransactionMapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AmexTransaction.
 */
@Service
@Transactional
public class AmexTransactionServiceImpl extends AbstractBaseTransactionService<AmexTransaction, AmexTransactionRepository> implements AmexTransactionService{

    private static final int REFERENCE_ID = 14;

	private static final int PERSON_INDEX = 3;

	private static final int AMOUNT_INDEX = 7;

	private static final int DESCRIPTION_INDEX = 2;

	private static final int DATE_INDEX = 0;

	private final Logger log = LoggerFactory.getLogger(AmexTransactionServiceImpl.class);
    
    @Inject
    private AmexTransactionRepository amexTransactionRepository;

    @Inject
    private AmexTransactionMapper amexTransactionMapper;

    @Inject
    private AmexTransactionSearchRepository amexTransactionSearchRepository;

    /**
     * Save a amexTransaction.
     *
     * @param amexTransactionDTO the entity to save
     * @return the persisted entity
     */
    public AmexTransactionDTO save(AmexTransactionDTO amexTransactionDTO) {
        log.debug("Request to save AmexTransaction : {}", amexTransactionDTO);
        AmexTransaction amexTransaction = amexTransactionMapper.amexTransactionDTOToAmexTransaction(amexTransactionDTO);
        amexTransaction = amexTransactionRepository.save(amexTransaction);
        AmexTransactionDTO result = amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction);
        amexTransactionSearchRepository.save(amexTransaction);
        return result;
    }
    
    @Override
    public void parseCsvAndSave(UploadedFiles uploadedFile) {
    	CSVParser parser;
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy  EEE");
    	List<AmexTransaction> transactions = new ArrayList<>();
		try {
			parser = CSVFormat.EXCEL.parse(
				    new InputStreamReader(new ByteArrayInputStream(uploadedFile.getFile()), "UTF8"));
	
    		for (CSVRecord record : parser) {
    				AmexTransaction transaction = new AmexTransaction();
	    		  try {
	    			  transaction.setDate(localDateFromString(record.get(DATE_INDEX), formatter));
	    			  transaction.setDescription(record.get(DESCRIPTION_INDEX));
	    			  transaction.setAmount(formatAmount(record.get(AMOUNT_INDEX)));
	    			  transaction.setPerson(record.get(PERSON_INDEX));
	    			  transaction.setReferenceId(Long.parseLong(record.get(REFERENCE_ID).replace("'", "")));
	    		
	    		  } catch (Exception e) {
	    		    throw new RuntimeException("Error at line "
	    		      + parser.getCurrentLineNumber(), e);
	    		  }
	    		  if (!transactionExist(transaction, amexTransactionRepository)) {
	    			  transactions.add(transaction);
	    		  }
    		}
    		parser.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		amexTransactionRepository.save(transactions);
    }


    /**
     *  Get all the amexTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AmexTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AmexTransactions");
        Page<AmexTransaction> result = amexTransactionRepository.findAll(pageable);
        return result.map(amexTransaction -> amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction));
    }

    /**
     *  Get one amexTransaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AmexTransactionDTO findOne(Long id) {
        log.debug("Request to get AmexTransaction : {}", id);
        AmexTransaction amexTransaction = amexTransactionRepository.findOne(id);
        AmexTransactionDTO amexTransactionDTO = amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction);
        return amexTransactionDTO;
    }

    /**
     *  Delete the  amexTransaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AmexTransaction : {}", id);
        amexTransactionRepository.delete(id);
        amexTransactionSearchRepository.delete(id);
    }

    /**
     * Search for the amexTransaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AmexTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AmexTransactions for query {}", query);
        Page<AmexTransaction> result = amexTransactionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(amexTransaction -> amexTransactionMapper.amexTransactionToAmexTransactionDTO(amexTransaction));
    }
}
