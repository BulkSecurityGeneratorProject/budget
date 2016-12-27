package com.budget.repository;

import com.budget.domain.AllyTransaction;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the AllyTransaction entity.
 */
@SuppressWarnings("unused")
public interface AllyTransactionRepository extends JpaRepository<AllyTransaction,Long> {
	
	public List<AllyTransaction> findByBudgeted(boolean budgeted);
	
}
