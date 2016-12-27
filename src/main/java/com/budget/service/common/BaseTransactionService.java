package com.budget.service.common;

import com.budget.domain.UploadedFiles;

public interface BaseTransactionService {
	void parseCsvAndSave(UploadedFiles uploadedFile);
}
