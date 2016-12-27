package com.budget.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.budget.domain.AllyTransaction;
import com.budget.domain.AmexTransaction;
import com.budget.domain.WellsFargoTransaction;
import com.budget.domain.common.Transactional;
import com.budget.domain.enumeration.Bank;
import com.budget.repository.AllyTransactionRepository;
import com.budget.repository.AmexTransactionRepository;
import com.budget.repository.WellsFargoTransactionRepository;
import com.budget.service.dto.TransactionBudgetDTO;

@Service
public class TransactionBudgetService {
	
	@Inject
	AllyTransactionRepository allyTransactionRepository;
	@Inject
	AmexTransactionRepository amexTransactionRepository;
	@Inject
	WellsFargoTransactionRepository wellsFargoTransactionRepository;

	public List<TransactionBudgetDTO> getTransactionsToBudget() {
		List<TransactionBudgetDTO> transactionList = new ArrayList<>();
		
		//Ally transactions
		List<AllyTransaction> allyTransactionList = allyTransactionRepository.findByBudgeted(false);
		for (AllyTransaction allyTransaction : allyTransactionList) {
			TransactionBudgetDTO dto = mapTransaction(allyTransaction);
			dto.setBank(Bank.ALLY);
			transactionList.add(dto);
		}
		
		//Amex transactions
		List<AmexTransaction> amexTransactionList = amexTransactionRepository.findByBudgeted(false);
		for (AmexTransaction amexTransaction : amexTransactionList) {
			TransactionBudgetDTO dto = mapTransaction(amexTransaction);
			dto.setBank(Bank.AMEX);
			transactionList.add(dto);
		}
		
		//Wells
		List<WellsFargoTransaction> wellsFargoTransactionList = wellsFargoTransactionRepository.findByBudgeted(false);
		for (WellsFargoTransaction wellsFargoTransaction : wellsFargoTransactionList) {
			TransactionBudgetDTO dto = mapTransaction(wellsFargoTransaction);
			dto.setBank(Bank.WELLS_FARGO);
			transactionList.add(dto);
		}
		
		return transactionList;
	}
	
	private TransactionBudgetDTO mapTransaction(Transactional transaction) {
		TransactionBudgetDTO dto = new TransactionBudgetDTO();
		dto.setAmount(transaction.getAmount());
		dto.setBudget(transaction.isBudgeted());
		dto.setDate(transaction.getLocalDate());
		dto.setDescription(transaction.getDescription());
		return dto;
	}
}
