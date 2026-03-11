package com.orderprocessing.orderprocessing.db.service;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Database Optimisation Enhancement: Database Metrics Service
 *
 * Tracks cache and query performance metrics for orderprocessing.
 * Exposed to Prometheus via /actuator/prometheus.
 *
 * Metrics:
 *  - orderprocessing_cache_hits_total       — Redis cache hits
 *  - orderprocessing_cache_misses_total     — Redis cache misses (DB queries)
 *  - orderprocessing_db_queries_total       — Total DB queries by type
 *  - orderprocessing_db_slow_queries_total  — Queries above 500ms
 *  - orderprocessing_connection_pool_active — HikariCP active connections
 */
@Service
public class DatabaseMetricsService {

    private final MeterRegistry meterRegistry;
    private final AtomicLong activeConnections = new AtomicLong(0);

    public DatabaseMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        Gauge.builder("orderprocessing.connection.pool.active", activeConnections, AtomicLong::get)
             .description("Active HikariCP connections for orderprocessing")
             .tag("service", "orderprocessing")
             .register(meterRegistry);
    }

    public void recordCacheHit(String cacheName) {
        Counter.builder("orderprocessing.cache.hits.total")
               .tag("service", "orderprocessing").tag("cache", cacheName)
               .description("Redis cache hits for orderprocessing")
               .register(meterRegistry).increment();
    }

    public void recordCacheMiss(String cacheName) {
        Counter.builder("orderprocessing.cache.misses.total")
               .tag("service", "orderprocessing").tag("cache", cacheName)
               .description("Redis cache misses for orderprocessing (DB fallback)")
               .register(meterRegistry).increment();
    }

    public void recordDbQuery(String queryType) {
        Counter.builder("orderprocessing.db.queries.total")
               .tag("service", "orderprocessing").tag("type", queryType)
               .description("DB queries for orderprocessing")
               .register(meterRegistry).increment();
    }

    public void recordSlowQuery(String queryType, long ms) {
        Counter.builder("orderprocessing.db.slow.queries.total")
               .tag("service", "orderprocessing").tag("type", queryType)
               .description("DB queries exceeding 500ms for orderprocessing")
               .register(meterRegistry).increment();
        meterRegistry.summary("orderprocessing.db.query.duration",
                "service", "orderprocessing", "type", queryType).record(ms);
    }

    public void setActiveConnections(long count) {
        activeConnections.set(count);
    }
}
