package com.budget.repository;

import com.budget.domain.BillType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BillType entity.
 */
@SuppressWarnings("unused")
public interface BillTypeRepository extends JpaRepository<BillType,Long> {

}
