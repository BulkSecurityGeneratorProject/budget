package com.budget.service.mapper;

import com.budget.domain.*;
import com.budget.service.dto.SpendingTypeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SpendingType and its DTO SpendingTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpendingTypeMapper {

    @Mapping(source = "spendingTransaction.id", target = "spendingTransactionId")
    SpendingTypeDTO spendingTypeToSpendingTypeDTO(SpendingType spendingType);

    List<SpendingTypeDTO> spendingTypesToSpendingTypeDTOs(List<SpendingType> spendingTypes);

    @Mapping(source = "spendingTransactionId", target = "spendingTransaction")
    SpendingType spendingTypeDTOToSpendingType(SpendingTypeDTO spendingTypeDTO);

    List<SpendingType> spendingTypeDTOsToSpendingTypes(List<SpendingTypeDTO> spendingTypeDTOs);

    default SpendingTransaction spendingTransactionFromId(Long id) {
        if (id == null) {
            return null;
        }
        SpendingTransaction spendingTransaction = new SpendingTransaction();
        spendingTransaction.setId(id);
        return spendingTransaction;
    }
}
