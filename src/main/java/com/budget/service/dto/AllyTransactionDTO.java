package com.budget.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.budget.domain.enumeration.AllyTransactionType;
import com.budget.domain.enumeration.AccountType;

/**
 * A DTO for the AllyTransaction entity.
 */
public class AllyTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private AllyTransactionType transactionType;

    @NotNull
    private String description;

    private Boolean budgeted;

    @NotNull
    private AccountType accountType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public AllyTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(AllyTransactionType transactionType) {
        this.transactionType = transactionType;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Boolean getBudgeted() {
        return budgeted;
    }

    public void setBudgeted(Boolean budgeted) {
        this.budgeted = budgeted;
    }
    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AllyTransactionDTO allyTransactionDTO = (AllyTransactionDTO) o;

        if ( ! Objects.equals(id, allyTransactionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AllyTransactionDTO{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", amount='" + amount + "'" +
            ", transactionType='" + transactionType + "'" +
            ", description='" + description + "'" +
            ", budgeted='" + budgeted + "'" +
            ", accountType='" + accountType + "'" +
            '}';
    }
}
