package com.budget.repository;

import com.budget.domain.WellsFargoTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WellsFargoTransaction entity.
 */
public interface WellsFargoTransactionRepository extends JpaRepository<WellsFargoTransaction,Long> {
	
	public List<WellsFargoTransaction> findByBudgeted(boolean budgeted);

}
