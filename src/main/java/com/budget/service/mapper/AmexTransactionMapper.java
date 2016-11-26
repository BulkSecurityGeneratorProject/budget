package com.budget.service.mapper;

import com.budget.domain.*;
import com.budget.service.dto.AmexTransactionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AmexTransaction and its DTO AmexTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AmexTransactionMapper {

    AmexTransactionDTO amexTransactionToAmexTransactionDTO(AmexTransaction amexTransaction);

    List<AmexTransactionDTO> amexTransactionsToAmexTransactionDTOs(List<AmexTransaction> amexTransactions);

    AmexTransaction amexTransactionDTOToAmexTransaction(AmexTransactionDTO amexTransactionDTO);

    List<AmexTransaction> amexTransactionDTOsToAmexTransactions(List<AmexTransactionDTO> amexTransactionDTOs);
}
