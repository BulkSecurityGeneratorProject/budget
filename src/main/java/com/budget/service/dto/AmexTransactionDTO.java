package com.budget.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * A DTO for the AmexTransaction entity.
 */
public class AmexTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private String description;

    private String person;

    @NotNull
    private BigDecimal amount;

    private Long referenceId;

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

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }
    public Boolean getBudgeted() {
        return budgeted;
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

        AmexTransactionDTO amexTransactionDTO = (AmexTransactionDTO) o;

        if ( ! Objects.equals(id, amexTransactionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AmexTransactionDTO{" +
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
