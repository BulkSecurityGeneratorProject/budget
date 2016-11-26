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
 * A BillTransaction.
 */
@Entity
@Table(name = "bill_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "billtransaction")
public class BillTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "amount", precision=10, scale=2)
    private BigDecimal amount;

    @Column(name = "day_out")
    private Integer dayOut;

    @Column(name = "from_account")
    private String fromAccount;

    @Column(name = "automatic")
    private Boolean automatic;

    @OneToMany(mappedBy = "billTransaction")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BillType> types = new HashSet<>();

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

    public BillTransaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BillTransaction amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDayOut() {
        return dayOut;
    }

    public BillTransaction dayOut(Integer dayOut) {
        this.dayOut = dayOut;
        return this;
    }

    public void setDayOut(Integer dayOut) {
        this.dayOut = dayOut;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public BillTransaction fromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
        return this;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Boolean isAutomatic() {
        return automatic;
    }

    public BillTransaction automatic(Boolean automatic) {
        this.automatic = automatic;
        return this;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    public Set<BillType> getTypes() {
        return types;
    }

    public BillTransaction types(Set<BillType> billTypes) {
        this.types = billTypes;
        return this;
    }

    public BillTransaction addType(BillType billType) {
        types.add(billType);
        billType.setBillTransaction(this);
        return this;
    }

    public BillTransaction removeType(BillType billType) {
        types.remove(billType);
        billType.setBillTransaction(null);
        return this;
    }

    public void setTypes(Set<BillType> billTypes) {
        this.types = billTypes;
    }

    public AllyTransaction getAllyTransaction() {
        return allyTransaction;
    }

    public BillTransaction allyTransaction(AllyTransaction allyTransaction) {
        this.allyTransaction = allyTransaction;
        return this;
    }

    public void setAllyTransaction(AllyTransaction allyTransaction) {
        this.allyTransaction = allyTransaction;
    }

    public AmexTransaction getAmexTransaction() {
        return amexTransaction;
    }

    public BillTransaction amexTransaction(AmexTransaction amexTransaction) {
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
        BillTransaction billTransaction = (BillTransaction) o;
        if(billTransaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, billTransaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BillTransaction{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", amount='" + amount + "'" +
            ", dayOut='" + dayOut + "'" +
            ", fromAccount='" + fromAccount + "'" +
            ", automatic='" + automatic + "'" +
            '}';
    }
}
