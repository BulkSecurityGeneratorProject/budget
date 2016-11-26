package com.budget.repository.search;

import com.budget.domain.BillTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BillTransaction entity.
 */
public interface BillTransactionSearchRepository extends ElasticsearchRepository<BillTransaction, Long> {
}
