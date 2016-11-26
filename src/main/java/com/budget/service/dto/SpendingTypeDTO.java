package com.budget.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.budget.domain.enumeration.SpendingMainType;

/**
 * A DTO for the SpendingType entity.
 */
public class SpendingTypeDTO implements Serializable {

    private Long id;

    private String name;

    private SpendingMainType mainType;


    private Long spendingTransactionId;
    
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
    public SpendingMainType getMainType() {
        return mainType;
    }

    public void setMainType(SpendingMainType mainType) {
        this.mainType = mainType;
    }

    public Long getSpendingTransactionId() {
        return spendingTransactionId;
    }

    public void setSpendingTransactionId(Long spendingTransactionId) {
        this.spendingTransactionId = spendingTransactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpendingTypeDTO spendingTypeDTO = (SpendingTypeDTO) o;

        if ( ! Objects.equals(id, spendingTypeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SpendingTypeDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", mainType='" + mainType + "'" +
            '}';
    }
}
