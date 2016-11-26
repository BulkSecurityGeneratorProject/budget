package com.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SpendingTransaction.
 */
@Entity
@Table(name = "spending_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "spendingtransaction")
public class SpendingTransaction implements Serializable {

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

    @OneToMany(mappedBy = "spendingTransaction")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SpendingType> types = new HashSet<>();

    @ManyToOne
    private AllyTransaction allyTransaction;

    @ManyToOne
    private AmexTransaction amexTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public SpendingTransaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public SpendingTransaction amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Set<SpendingType> getTypes() {
        return types;
    }

    public SpendingTransaction types(Set<SpendingType> spendingTypes) {
        this.types = spendingTypes;
        return this;
    }

    public SpendingTransaction addType(SpendingType spendingType) {
        types.add(spendingType);
        spendingType.setSpendingTransaction(this);
        return this;
    }

    public SpendingTransaction removeType(SpendingType spendingType) {
        types.remove(spendingType);
        spendingType.setSpendingTransaction(null);
        return this;
    }

    public void setTypes(Set<SpendingType> spendingTypes) {
        this.types = spendingTypes;
    }

    public AllyTransaction getAllyTransaction() {
        return allyTransaction;
    }

    public SpendingTransaction allyTransaction(AllyTransaction allyTransaction) {
        this.allyTransaction = allyTransaction;
        return this;
    }

    public void setAllyTransaction(AllyTransaction allyTransaction) {
        this.allyTransaction = allyTransaction;
    }

    public AmexTransaction getAmexTransaction() {
        return amexTransaction;
    }

    public SpendingTransaction amexTransaction(AmexTransaction amexTransaction) {
        this.amexTransaction = amexTransaction;
        return this;
    }

    public void setAmexTransaction(AmexTransaction amexTransaction) {
        this.amexTransaction = amexTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpendingTransaction spendingTransaction = (SpendingTransaction) o;
        if(spendingTransaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, spendingTransaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SpendingTransaction{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
