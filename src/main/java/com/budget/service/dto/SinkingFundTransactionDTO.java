package com.budget.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.budget.domain.enumeration.SinkingFundType;

/**
 * A DTO for the SinkingFundTransaction entity.
 */
public class SinkingFundTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private SinkingFundType type;

    @NotNull
    private BigDecimal amount;


    private Long allyTransactionId;
    
    private Long amexTranactionId;
    
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
    public SinkingFundType getType() {
        return type;
    }

    public void setType(SinkingFundType type) {
        this.type = type;
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

    public Long getAmexTranactionId() {
        return amexTranactionId;
    }

    public void setAmexTranactionId(Long amexTransactionId) {
        this.amexTranactionId = amexTransactionId;
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

        SinkingFundTransactionDTO sinkingFundTransactionDTO = (SinkingFundTransactionDTO) o;

        if ( ! Objects.equals(id, sinkingFundTransactionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SinkingFundTransactionDTO{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", type='" + type + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
