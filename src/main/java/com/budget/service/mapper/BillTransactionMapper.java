package com.budget.service.mapper;

import com.budget.domain.*;
import com.budget.service.dto.BillTransactionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BillTransaction and its DTO BillTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BillTransactionMapper {

    @Mapping(source = "allyTransaction.id", target = "allyTransactionId")
    @Mapping(source = "amexTransaction.id", target = "amexTransactionId")
    BillTransactionDTO billTransactionToBillTransactionDTO(BillTransaction billTransaction);

    List<BillTransactionDTO> billTransactionsToBillTransactionDTOs(List<BillTransaction> billTransactions);

    @Mapping(target = "types", ignore = true)
    @Mapping(source = "allyTransactionId", target = "allyTransaction")
    @Mapping(source = "amexTransactionId", target = "amexTransaction")
    BillTransaction billTransactionDTOToBillTransaction(BillTransactionDTO billTransactionDTO);

    List<BillTransaction> billTransactionDTOsToBillTransactions(List<BillTransactionDTO> billTransactionDTOs);

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
