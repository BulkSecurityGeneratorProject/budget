package com.budget.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.budget.domain.enumeration.BillMainType;

/**
 * A DTO for the BillType entity.
 */
public class BillTypeDTO implements Serializable {

    private Long id;

    private String name;

    private BillMainType mainType;


    private Long billTransactionId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public BillMainType getMainType() {
        return mainType;
    }

    public void setMainType(BillMainType mainType) {
        this.mainType = mainType;
    }

    public Long getBillTransactionId() {
        return billTransactionId;
    }

    public void setBillTransactionId(Long billTransactionId) {
        this.billTransactionId = billTransactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BillTypeDTO billTypeDTO = (BillTypeDTO) o;

        if ( ! Objects.equals(id, billTypeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BillTypeDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", mainType='" + mainType + "'" +
            '}';
    }
}
