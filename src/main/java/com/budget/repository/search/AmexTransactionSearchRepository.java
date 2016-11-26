package com.budget.repository.search;

import com.budget.domain.AmexTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AmexTransaction entity.
 */
public interface AmexTransactionSearchRepository extends ElasticsearchRepository<AmexTransaction, Long> {
}
