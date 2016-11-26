package com.budget.repository;

import com.budget.domain.SpendingType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SpendingType entity.
 */
@SuppressWarnings("unused")
public interface SpendingTypeRepository extends JpaRepository<SpendingType,Long> {

}
