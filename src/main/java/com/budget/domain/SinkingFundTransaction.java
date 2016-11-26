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

import com.budget.domain.enumeration.SinkingFundType;

/**
 * A SinkingFundTransaction.
 */
@Entity
@Table(name = "sinking_fund_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sinkingfundtransaction")
public class SinkingFundTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SinkingFundType type;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @ManyToOne
    private AllyTransaction allyTransaction;

    @ManyToOne
    private AmexTransaction amexTranaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public SinkingFundTransaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public SinkingFundType getType() {
        return type;
    }

    public SinkingFundTransaction type(SinkingFundType type) {
        this.type = type;
        return this;
    }

    public void setType(SinkingFundType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public SinkingFundTransaction amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AllyTransaction getAllyTransaction() {
        return allyTransaction;
    }

    public SinkingFundTransaction allyTransaction(AllyTransaction allyTransaction) {
        this.allyTransaction = allyTransaction;
        return this;
    }

    public void setAllyTransaction(AllyTransaction allyTransaction) {
        this.allyTransaction = allyTransaction;
    }

    public AmexTransaction getAmexTranaction() {
        return amexTranaction;
    }

    public SinkingFundTransaction amexTranaction(AmexTransaction amexTransaction) {
        this.amexTranaction = amexTransaction;
        return this;
    }

    public void setAmexTranaction(AmexTransaction amexTransaction) {
        this.amexTranaction = amexTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SinkingFundTransaction sinkingFundTransaction = (SinkingFundTransaction) o;
        if(sinkingFundTransaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sinkingFundTransaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SinkingFundTransaction{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", type='" + type + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
