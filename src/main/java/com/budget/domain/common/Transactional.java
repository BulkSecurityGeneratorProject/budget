package com.budget.domain.common;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Transactional {
	Long getId();
	BigDecimal getAmount();
	String getDescription();
	LocalDate getLocalDate();
	Boolean isBudgeted();
}
