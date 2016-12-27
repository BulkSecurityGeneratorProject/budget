package com.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.budget.domain.common.Transactional;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A AmexTransaction.
 */
@Entity
@Table(name = "amex_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "amextransaction")
public class AmexTransaction implements Transactional, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "person")
    private String person;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "budgeted")
    private Boolean budgeted;

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

    public AmexTransaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public AmexTransaction description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPerson() {
        return person;
    }

    public AmexTransaction person(String person) {
        this.person = person;
        return this;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AmexTransaction amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public AmexTransaction referenceId(Long referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public Boolean isBudgeted() {
        return budgeted;
    }

    public AmexTransaction budgeted(Boolean budgeted) {
        this.budgeted = budgeted;
        return this;
    }

    public void setBudgeted(Boolean budgeted) {
        this.budgeted = budgeted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AmexTransaction amexTransaction = (AmexTransaction) o;
        if(amexTransaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, amexTransaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AmexTransaction{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", description='" + description + "'" +
            ", person='" + person + "'" +
            ", amount='" + amount + "'" +
            ", referenceId='" + referenceId + "'" +
            ", budgeted='" + budgeted + "'" +
            '}';
    }
}
