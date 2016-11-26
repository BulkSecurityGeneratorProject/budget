package com.budget.repository;

import com.budget.domain.AmexTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AmexTransaction entity.
 */
@SuppressWarnings("unused")
public interface AmexTransactionRepository extends JpaRepository<AmexTransaction,Long> {

}
