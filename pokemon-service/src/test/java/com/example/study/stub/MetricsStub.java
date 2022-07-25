package com.example.study.stub;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

public class MetricsStub {
  
  public static Counter counterStub() {
    return Counter.builder("Teste")
             .tag("Service", "Teste service")
             .description("Requisições")
             .register(new SimpleMeterRegistry());
  }
}
