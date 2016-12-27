package com.budget.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the SpendingTransaction entity.
 */
public class SpendingTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private BigDecimal amount;


    private Long allyTransactionId;
    
    private Long amexTransactionId;
    
    private Long wellsFargoTransactionId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getAllyTransactionId() {
        return allyTransactionId;
    }

    public void setAllyTransactionId(Long allyTransactionId) {
        this.allyTransactionId = allyTransactionId;
    }

    public Long getAmexTransactionId() {
        return amexTransactionId;
    }

    public void setAmexTransactionId(Long amexTransactionId) {
        this.amexTransactionId = amexTransactionId;
    }

    public Long getWellsFargoTransactionId() {
        return wellsFargoTransactionId;
    }

    public void setWellsFargoTransactionId(Long wellsFargoTransactionId) {
        this.wellsFargoTransactionId = wellsFargoTransactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpendingTransactionDTO spendingTransactionDTO = (SpendingTransactionDTO) o;

        if ( ! Objects.equals(id, spendingTransactionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SpendingTransactionDTO{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
