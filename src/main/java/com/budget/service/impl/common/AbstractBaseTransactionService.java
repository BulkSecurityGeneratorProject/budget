package com.budget.service.impl.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import com.budget.domain.UploadedFiles;
import com.budget.service.common.BaseTransactionService;

public abstract class AbstractBaseTransactionService<T extends Serializable, R extends JpaRepository<T,Long>> implements BaseTransactionService {
	public abstract void parseCsvAndSave(UploadedFiles uploadedFile);
	
	protected boolean transactionExist(T transaction, R repository) {
    	Example<T> example = Example.of(transaction);
		return repository.exists(example);
    }
	
	protected LocalDate localDateFromString(String date, DateTimeFormatter formatter) {
		return LocalDate.parse(date.replaceAll("'", ""), formatter);
	}
	
	protected BigDecimal formatAmount(String amount) {
    	if (amount != null) {
    		amount.replace("$", "").trim();
    	}
    	return new BigDecimal(amount);
    }
	
}
