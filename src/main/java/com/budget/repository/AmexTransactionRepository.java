package com.budget.repository;

import com.budget.domain.AmexTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AmexTransaction entity.
 */
public interface AmexTransactionRepository extends JpaRepository<AmexTransaction,Long> {
	
	public List<AmexTransaction> findByBudgeted(boolean budgeted);

}
