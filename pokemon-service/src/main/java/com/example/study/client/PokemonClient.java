package com.example.study.client;

import com.example.study.exception.NotFoundException;
import com.example.study.factory.CounterFactory;
import com.example.study.model.PokemonModel;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class PokemonClient {
  
  public PokemonModel search(final Integer id, final Counter onlineCounter, Counter errorCounter) {
    try {
      log.info("Searching online...");
      var url = String.format("https://pokeapi.co/api/v2/pokemon/%s", id);
      PokemonModel pokemonModel = (new RestTemplate()).getForObject(url, PokemonModel.class);
      onlineCounter.increment();
      return pokemonModel;
    } catch (Exception ex) {
      log.error("Error searching for id: {}", id);
      errorCounter.increment();
      throw new NotFoundException();
    }
  }
}
