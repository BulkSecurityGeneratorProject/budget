package com.budget.repository.search;

import com.budget.domain.AllyTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AllyTransaction entity.
 */
public interface AllyTransactionSearchRepository extends ElasticsearchRepository<AllyTransaction, Long> {
}
