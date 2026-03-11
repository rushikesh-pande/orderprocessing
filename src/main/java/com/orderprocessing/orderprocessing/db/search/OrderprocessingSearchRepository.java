package com.orderprocessing.orderprocessing.db.search;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Database Optimisation Enhancement: Elasticsearch Search Repository
 * Provides full-text search over orderprocessing entities.
 */
@Repository
public interface OrderprocessingSearchRepository
        extends ElasticsearchRepository<OrderprocessingSearchDocument, String> {

    List<OrderprocessingSearchDocument> findByNameContainingOrDescriptionContaining(
            String name, String description);

    List<OrderprocessingSearchDocument> findByStatus(String status);

    List<OrderprocessingSearchDocument> findByCategory(String category);

    @Query("{\"multi_match\":{\"query\":\"?0\",\"fields\":[\"name^2\",\"description\",\"category\"],\"fuzziness\":\"AUTO\",\"type\":\"best_fields\"}}")
    List<OrderprocessingSearchDocument> fuzzySearch(String query);
}
