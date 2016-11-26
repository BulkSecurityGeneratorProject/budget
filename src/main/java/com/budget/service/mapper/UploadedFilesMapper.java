package com.budget.service.mapper;

import com.budget.domain.*;
import com.budget.service.dto.UploadedFilesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity UploadedFiles and its DTO UploadedFilesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UploadedFilesMapper {

    UploadedFilesDTO uploadedFilesToUploadedFilesDTO(UploadedFiles uploadedFiles);

    List<UploadedFilesDTO> uploadedFilesToUploadedFilesDTOs(List<UploadedFiles> uploadedFiles);

    UploadedFiles uploadedFilesDTOToUploadedFiles(UploadedFilesDTO uploadedFilesDTO);

    List<UploadedFiles> uploadedFilesDTOsToUploadedFiles(List<UploadedFilesDTO> uploadedFilesDTOs);
}
