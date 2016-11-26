package com.budget.repository.search;

import com.budget.domain.SinkingFundTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SinkingFundTransaction entity.
 */
public interface SinkingFundTransactionSearchRepository extends ElasticsearchRepository<SinkingFundTransaction, Long> {
}
