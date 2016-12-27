package com.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.budget.domain.common.Transactional;
import com.budget.domain.enumeration.AccountType;

/**
 * A WellsFargoTransaction.
 */
@Entity
@Table(name = "wells_fargo_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "wellsfargotransaction")
public class WellsFargoTransaction implements Transactional, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "budgeted")
    private Boolean budgeted;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public LocalDate getLocalDate() {
    	return date;
    }

    public WellsFargoTransaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public WellsFargoTransaction amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public WellsFargoTransaction description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isBudgeted() {
        return budgeted;
    }

    public WellsFargoTransaction budgeted(Boolean budgeted) {
        this.budgeted = budgeted;
        return this;
    }

    public void setBudgeted(Boolean budgeted) {
        this.budgeted = budgeted;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public WellsFargoTransaction accountType(AccountType accountType) {
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
        WellsFargoTransaction wellsFargoTransaction = (WellsFargoTransaction) o;
        if(wellsFargoTransaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wellsFargoTransaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WellsFargoTransaction{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", amount='" + amount + "'" +
            ", description='" + description + "'" +
            ", budgeted='" + budgeted + "'" +
            ", accountType='" + accountType + "'" +
            '}';
    }
}
