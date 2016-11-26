package com.budget.repository;

import com.budget.domain.SinkingFundTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SinkingFundTransaction entity.
 */
@SuppressWarnings("unused")
public interface SinkingFundTransactionRepository extends JpaRepository<SinkingFundTransaction,Long> {

}
