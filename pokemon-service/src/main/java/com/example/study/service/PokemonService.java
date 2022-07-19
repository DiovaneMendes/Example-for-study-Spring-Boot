package com.example.study.service;

import com.example.study.client.PokemonClient;
import com.example.study.factory.CounterFactory;
import com.example.study.model.PokemonModel;
import com.example.study.repository.PokemonRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PokemonService {
  
  private final PokemonClient client;
  private final PokemonRepository repository;
  private final Counter cacheCounter, errorCounter, onlineCounter;
  
  public PokemonService(final PokemonClient client,
                        final MeterRegistry meterRegistry,
                        final PokemonRepository repository) {
    this.client = client;
    this.repository = repository;
    this.cacheCounter = CounterFactory.generateCounter(meterRegistry, "cache");
    this.errorCounter = CounterFactory.generateCounter(meterRegistry, "error");
    this.onlineCounter = CounterFactory.generateCounter(meterRegistry, "online");
  }
  
  public PokemonModel search(final Integer id) {
    log.info("Searching pokémon.");
    var optionalPokemon = repository.findById(id);
    
    if (optionalPokemon.isPresent()) {
      cacheCounter.increment();
      return optionalPokemon.get();
    }
    
    return client.search(id, onlineCounter, errorCounter);
  }
}