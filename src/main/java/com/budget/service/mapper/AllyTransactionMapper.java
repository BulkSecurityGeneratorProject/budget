package com.budget.service.mapper;

import com.budget.domain.*;
import com.budget.service.dto.AllyTransactionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AllyTransaction and its DTO AllyTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AllyTransactionMapper {

    AllyTransactionDTO allyTransactionToAllyTransactionDTO(AllyTransaction allyTransaction);

    List<AllyTransactionDTO> allyTransactionsToAllyTransactionDTOs(List<AllyTransaction> allyTransactions);

    AllyTransaction allyTransactionDTOToAllyTransaction(AllyTransactionDTO allyTransactionDTO);

    List<AllyTransaction> allyTransactionDTOsToAllyTransactions(List<AllyTransactionDTO> allyTransactionDTOs);
}
