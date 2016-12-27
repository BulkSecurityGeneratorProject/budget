package com.budget.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the BillTransaction entity.
 */
public class BillTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private BigDecimal amount;

    private Integer dayOut;

    private String fromAccount;

    private Boolean automatic;


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
    public Integer getDayOut() {
        return dayOut;
    }

    public void setDayOut(Integer dayOut) {
        this.dayOut = dayOut;
    }
    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }
    public Boolean getAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
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

        BillTransactionDTO billTransactionDTO = (BillTransactionDTO) o;

        if ( ! Objects.equals(id, billTransactionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BillTransactionDTO{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", amount='" + amount + "'" +
            ", dayOut='" + dayOut + "'" +
            ", fromAccount='" + fromAccount + "'" +
            ", automatic='" + automatic + "'" +
            '}';
    }
}
