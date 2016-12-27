package com.budget.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.budget.domain.AllyTransaction;
import com.budget.domain.UploadedFiles;
import com.budget.domain.WellsFargoTransaction;
import com.budget.domain.enumeration.AllyTransactionType;
import com.budget.repository.WellsFargoTransactionRepository;
import com.budget.repository.search.WellsFargoTransactionSearchRepository;
import com.budget.service.WellsFargoTransactionService;
import com.budget.service.impl.common.AbstractBaseTransactionService;

/**
 * Service Implementation for managing WellsFargoTransaction.
 */
@Service
@Transactional
public class WellsFargoTransactionServiceImpl
		extends AbstractBaseTransactionService<WellsFargoTransaction, WellsFargoTransactionRepository>
		implements WellsFargoTransactionService {

	private static final int AMOUNT_INDEX = 1;

	private static final int DESCRIPTION_INDEX = 4;

	private static final int DATE_INDEX = 0;

	private final Logger log = LoggerFactory.getLogger(WellsFargoTransactionService.class);

	@Inject
	private WellsFargoTransactionRepository wellsFargoTransactionRepository;

	@Inject
	private WellsFargoTransactionSearchRepository wellsFargoTransactionSearchRepository;

	/**
	 * Save a wellsFargoTransaction.
	 *
	 * @param wellsFargoTransaction
	 *            the entity to save
	 * @return the persisted entity
	 */
	public WellsFargoTransaction save(WellsFargoTransaction wellsFargoTransaction) {
		log.debug("Request to save WellsFargoTransaction : {}", wellsFargoTransaction);
		WellsFargoTransaction result = wellsFargoTransactionRepository.save(wellsFargoTransaction);
		wellsFargoTransactionSearchRepository.save(result);
		return result;
	}

	/**
	 * Get all the wellsFargoTransactions.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<WellsFargoTransaction> findAll(Pageable pageable) {
		log.debug("Request to get all WellsFargoTransactions");
		Page<WellsFargoTransaction> result = wellsFargoTransactionRepository.findAll(pageable);
		return result;
	}

	/**
	 * Get one wellsFargoTransaction by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public WellsFargoTransaction findOne(Long id) {
		log.debug("Request to get WellsFargoTransaction : {}", id);
		WellsFargoTransaction wellsFargoTransaction = wellsFargoTransactionRepository.findOne(id);
		return wellsFargoTransaction;
	}

	/**
	 * Delete the wellsFargoTransaction by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete WellsFargoTransaction : {}", id);
		wellsFargoTransactionRepository.delete(id);
		wellsFargoTransactionSearchRepository.delete(id);
	}

	/**
	 * Search for the wellsFargoTransaction corresponding to the query.
	 *
	 * @param query
	 *            the query of the search
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<WellsFargoTransaction> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of WellsFargoTransactions for query {}", query);
		Page<WellsFargoTransaction> result = wellsFargoTransactionSearchRepository.search(queryStringQuery(query),
				pageable);
		return result;
	}

	@Override
	public void parseCsvAndSave(UploadedFiles uploadedFile) {
		CSVParser parser;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		List<WellsFargoTransaction> transactions = new ArrayList<>();
		try {
			parser = CSVFormat.EXCEL
					.parse(new InputStreamReader(new ByteArrayInputStream(uploadedFile.getFile()), "UTF8"));

			for (CSVRecord record : parser) {
				if (record.get(0) == null || record.get(0).equals("")) {
					break;
				}
				WellsFargoTransaction transaction = new WellsFargoTransaction();
				try {
					transaction.setDate(localDateFromString(record.get(DATE_INDEX), formatter));
					transaction.setAmount(formatAmount(record.get(AMOUNT_INDEX)));
					transaction.setDescription(record.get(DESCRIPTION_INDEX));
					transaction.setAccountType(uploadedFile.getAccountType());

				} catch (Exception e) {
					throw new RuntimeException("Error at line " + parser.getCurrentLineNumber(), e);
				}
				if (!transactionExist(transaction, wellsFargoTransactionRepository)) {
					transactions.add(transaction);
				}

			}
			parser.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		wellsFargoTransactionRepository.save(transactions);
	}

}
