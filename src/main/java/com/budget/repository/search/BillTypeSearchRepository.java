package com.budget.repository.search;

import com.budget.domain.BillType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BillType entity.
 */
public interface BillTypeSearchRepository extends ElasticsearchRepository<BillType, Long> {
}
