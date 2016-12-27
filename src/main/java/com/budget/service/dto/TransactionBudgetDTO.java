package com.budget.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.budget.domain.enumeration.Bank;

public class TransactionBudgetDTO implements Serializable {
	private Bank bank;
	private Long id;
	private boolean budget;
	private BigDecimal amount;
	private String description;
	private LocalDate date;
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isBudget() {
		return budget;
	}
	public void setBudget(boolean budget) {
		this.budget = budget;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	

}
