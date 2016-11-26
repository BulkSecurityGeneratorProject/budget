package com.budget.repository.search;

import com.budget.domain.SpendingType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SpendingType entity.
 */
public interface SpendingTypeSearchRepository extends ElasticsearchRepository<SpendingType, Long> {
}
