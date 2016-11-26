package com.budget.service.dto;

import java.util.Arrays;

public class UploadDTO {

	String bank;
	byte[] file;
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result + Arrays.hashCode(file);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UploadDTO other = (UploadDTO) obj;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (!Arrays.equals(file, other.file))
			return false;
		return true;
	}
	
    @Override
    public String toString() {
        return "UploadDTO{" +
            "bank=" + bank +
            ", file='" + file.toString() + "'" +
            '}';
    }
}
