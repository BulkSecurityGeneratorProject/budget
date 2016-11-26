package com.budget.service.mapper;

import com.budget.domain.*;
import com.budget.service.dto.SpendingTransactionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SpendingTransaction and its DTO SpendingTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpendingTransactionMapper {

    @Mapping(source = "allyTransaction.id", target = "allyTransactionId")
    @Mapping(source = "amexTransaction.id", target = "amexTransactionId")
    SpendingTransactionDTO spendingTransactionToSpendingTransactionDTO(SpendingTransaction spendingTransaction);

    List<SpendingTransactionDTO> spendingTransactionsToSpendingTransactionDTOs(List<SpendingTransaction> spendingTransactions);

    @Mapping(target = "types", ignore = true)
    @Mapping(source = "allyTransactionId", target = "allyTransaction")
    @Mapping(source = "amexTransactionId", target = "amexTransaction")
    SpendingTransaction spendingTransactionDTOToSpendingTransaction(SpendingTransactionDTO spendingTransactionDTO);

    List<SpendingTransaction> spendingTransactionDTOsToSpendingTransactions(List<SpendingTransactionDTO> spendingTransactionDTOs);

    default AllyTransaction allyTransactionFromId(Long id) {
        if (id == null) {
            return null;
        }
        AllyTransaction allyTransaction = new AllyTransaction();
        allyTransaction.setId(id);
        return allyTransaction;
    }

    default AmexTransaction amexTransactionFromId(Long id) {
        if (id == null) {
            return null;
        }
        AmexTransaction amexTransaction = new AmexTransaction();
        amexTransaction.setId(id);
        return amexTransaction;
    }
}
