package com.budget.repository.search;

import com.budget.domain.WellsFargoTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WellsFargoTransaction entity.
 */
public interface WellsFargoTransactionSearchRepository extends ElasticsearchRepository<WellsFargoTransaction, Long> {
}
