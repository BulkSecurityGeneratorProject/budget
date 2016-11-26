package com.budget.repository.search;

import com.budget.domain.UploadedFiles;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UploadedFiles entity.
 */
public interface UploadedFilesSearchRepository extends ElasticsearchRepository<UploadedFiles, Long> {
}
