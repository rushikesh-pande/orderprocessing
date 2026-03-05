package com.order.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-added by Hub-Spoke Orchestrator v3.0 — orderprocessing
 */
@Configuration
public class HubSpokeActuatorConfig {
    @Bean
    MeterRegistryCustomizer<MeterRegistry> hubSpokeMetricsV3() {
        return reg -> reg.config().commonTags(
            "app",       "orderprocessing",
            "agent",     "hub_spoke_orchestrator_v3",
            "discovery", "github_api_keyword_score"
        );
    }
}
