package com.budget.repository;

import com.budget.domain.BillTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BillTransaction entity.
 */
@SuppressWarnings("unused")
public interface BillTransactionRepository extends JpaRepository<BillTransaction,Long> {

}
