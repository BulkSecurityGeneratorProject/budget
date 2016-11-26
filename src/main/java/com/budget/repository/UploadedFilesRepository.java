package com.budget.repository;

import com.budget.domain.UploadedFiles;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UploadedFiles entity.
 */
@SuppressWarnings("unused")
public interface UploadedFilesRepository extends JpaRepository<UploadedFiles,Long> {

}
