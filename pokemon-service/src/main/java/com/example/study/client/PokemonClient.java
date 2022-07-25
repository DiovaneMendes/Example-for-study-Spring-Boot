package com.example.study.client;

import com.example.study.exception.NotFoundException;
import com.example.study.model.PokemonModel;
import io.micrometer.core.instrument.Counter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class PokemonClient {
  
  private final RestTemplate restTemplate;
  
  public PokemonClient() {
    this.restTemplate = new RestTemplate();
  }
  
  public PokemonModel searchById(final Integer id, final Counter errorCounter) {
    try {
      var url = String.format("https://pokeapi.co/api/v2/pokemon/%s", id);
      return restTemplate.getForObject(url, PokemonModel.class);
    } catch (Exception ex) {
      log.error("Error searching for id: {}", id);
      errorCounter.increment();
      throw new NotFoundException();
    }
  }
}
