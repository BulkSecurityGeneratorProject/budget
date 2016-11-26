package com.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.budget.domain.enumeration.AllyTransactionType;

import com.budget.domain.enumeration.AccountType;

/**
 * A AllyTransaction.
 */
@Entity
@Table(name = "ally_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "allytransaction")
public class AllyTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private AllyTransactionType transactionType;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "budgeted")
    private Boolean budgeted;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
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

    public AllyTransaction date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AllyTransaction amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AllyTransactionType getTransactionType() {
        return transactionType;
    }

    public AllyTransaction transactionType(AllyTransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(AllyTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public AllyTransaction description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isBudgeted() {
        return budgeted;
    }

    public AllyTransaction budgeted(Boolean budgeted) {
        this.budgeted = budgeted;
        return this;
    }

    public void setBudgeted(Boolean budgeted) {
        this.budgeted = budgeted;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public AllyTransaction accountType(AccountType accountType) {
        this.accountType = accountType;
        return this;
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
        AllyTransaction allyTransaction = (AllyTransaction) o;
        if(allyTransaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, allyTransaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AllyTransaction{" +
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
