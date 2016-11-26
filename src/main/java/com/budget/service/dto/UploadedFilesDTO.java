package com.budget.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

import com.budget.domain.enumeration.Bank;
import com.budget.domain.enumeration.AccountType;

/**
 * A DTO for the UploadedFiles entity.
 */
public class UploadedFilesDTO implements Serializable {

    private Long id;

    private LocalDate uploadDate;

    @NotNull
    private Bank bank;

    @NotNull
    @Lob
    private byte[] file;

    private String fileContentType;
    @NotNull
    private AccountType accountType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
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

        UploadedFilesDTO uploadedFilesDTO = (UploadedFilesDTO) o;

        if ( ! Objects.equals(id, uploadedFilesDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UploadedFilesDTO{" +
            "id=" + id +
            ", uploadDate='" + uploadDate + "'" +
            ", bank='" + bank + "'" +
            ", file='" + file + "'" +
            ", accountType='" + accountType + "'" +
            '}';
    }
}
