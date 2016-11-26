package com.budget.service.mapper;

import com.budget.domain.*;
import com.budget.service.dto.BillTypeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BillType and its DTO BillTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BillTypeMapper {

    @Mapping(source = "billTransaction.id", target = "billTransactionId")
    BillTypeDTO billTypeToBillTypeDTO(BillType billType);

    List<BillTypeDTO> billTypesToBillTypeDTOs(List<BillType> billTypes);

    @Mapping(source = "billTransactionId", target = "billTransaction")
    BillType billTypeDTOToBillType(BillTypeDTO billTypeDTO);

    List<BillType> billTypeDTOsToBillTypes(List<BillTypeDTO> billTypeDTOs);

    default BillTransaction billTransactionFromId(Long id) {
        if (id == null) {
            return null;
        }
        BillTransaction billTransaction = new BillTransaction();
        billTransaction.setId(id);
        return billTransaction;
    }
}
