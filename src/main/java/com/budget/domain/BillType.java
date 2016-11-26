package com.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.budget.domain.enumeration.BillMainType;

/**
 * A BillType.
 */
@Entity
@Table(name = "bill_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "billtype")
public class BillType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_type")
    private BillMainType mainType;

    @ManyToOne
    private BillTransaction billTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BillType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BillMainType getMainType() {
        return mainType;
    }

    public BillType mainType(BillMainType mainType) {
        this.mainType = mainType;
        return this;
    }

    public void setMainType(BillMainType mainType) {
        this.mainType = mainType;
    }

    public BillTransaction getBillTransaction() {
        return billTransaction;
    }

    public BillType billTransaction(BillTransaction billTransaction) {
        this.billTransaction = billTransaction;
        return this;
    }

    public void setBillTransaction(BillTransaction billTransaction) {
        this.billTransaction = billTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BillType billType = (BillType) o;
        if(billType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, billType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BillType{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", mainType='" + mainType + "'" +
            '}';
    }
}
