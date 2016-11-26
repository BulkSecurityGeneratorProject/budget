package com.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.budget.domain.enumeration.SpendingMainType;

/**
 * A SpendingType.
 */
@Entity
@Table(name = "spending_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "spendingtype")
public class SpendingType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_type")
    private SpendingMainType mainType;

    @ManyToOne
    private SpendingTransaction spendingTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SpendingType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpendingMainType getMainType() {
        return mainType;
    }

    public SpendingType mainType(SpendingMainType mainType) {
        this.mainType = mainType;
        return this;
    }

    public void setMainType(SpendingMainType mainType) {
        this.mainType = mainType;
    }

    public SpendingTransaction getSpendingTransaction() {
        return spendingTransaction;
    }

    public SpendingType spendingTransaction(SpendingTransaction spendingTransaction) {
        this.spendingTransaction = spendingTransaction;
        return this;
    }

    public void setSpendingTransaction(SpendingTransaction spendingTransaction) {
        this.spendingTransaction = spendingTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpendingType spendingType = (SpendingType) o;
        if(spendingType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, spendingType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SpendingType{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", mainType='" + mainType + "'" +
            '}';
    }
}
