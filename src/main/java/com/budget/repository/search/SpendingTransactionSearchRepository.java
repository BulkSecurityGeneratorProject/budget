package com.budget.repository.search;

import com.budget.domain.SpendingTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SpendingTransaction entity.
 */
public interface SpendingTransactionSearchRepository extends ElasticsearchRepository<SpendingTransaction, Long> {
}
