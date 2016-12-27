package com.budget.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.budget.service.TransactionBudgetService;
import com.budget.service.dto.TransactionBudgetDTO;
import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class TransactionBudgetResource {
    private final Logger log = LoggerFactory.getLogger(TransactionBudgetResource.class);
	
	@Inject
	TransactionBudgetService transactionBudgetService;
	
	@GetMapping("/transactions-budget")
    @Timed
    public List<TransactionBudgetDTO> getAllTransactionBudget() {
        log.debug("REST request to get all Transactions to budget");
        return transactionBudgetService.getTransactionsToBudget();
    }
}
