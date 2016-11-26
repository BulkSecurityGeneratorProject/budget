package com.budget.service.mapper;

import com.budget.domain.*;
import com.budget.service.dto.SinkingFundTransactionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SinkingFundTransaction and its DTO SinkingFundTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SinkingFundTransactionMapper {

    @Mapping(source = "allyTransaction.id", target = "allyTransactionId")
    @Mapping(source = "amexTranaction.id", target = "amexTranactionId")
    SinkingFundTransactionDTO sinkingFundTransactionToSinkingFundTransactionDTO(SinkingFundTransaction sinkingFundTransaction);

    List<SinkingFundTransactionDTO> sinkingFundTransactionsToSinkingFundTransactionDTOs(List<SinkingFundTransaction> sinkingFundTransactions);

    @Mapping(source = "allyTransactionId", target = "allyTransaction")
    @Mapping(source = "amexTranactionId", target = "amexTranaction")
    SinkingFundTransaction sinkingFundTransactionDTOToSinkingFundTransaction(SinkingFundTransactionDTO sinkingFundTransactionDTO);

    List<SinkingFundTransaction> sinkingFundTransactionDTOsToSinkingFundTransactions(List<SinkingFundTransactionDTO> sinkingFundTransactionDTOs);

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
