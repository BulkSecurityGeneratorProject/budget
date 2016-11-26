package com.budget.repository;

import com.budget.domain.SpendingTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SpendingTransaction entity.
 */
@SuppressWarnings("unused")
public interface SpendingTransactionRepository extends JpaRepository<SpendingTransaction,Long> {

}
