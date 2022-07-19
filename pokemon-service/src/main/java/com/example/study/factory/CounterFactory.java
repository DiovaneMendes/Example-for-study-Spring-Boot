package com.example.study.factory;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CounterFactory {
  
  public static Counter generateCounter(final MeterRegistry registry, final String outcomeValue) {
    return Counter.builder("service_metrics")
             .tag("outcome", outcomeValue)
             .register(registry);
  }
}
