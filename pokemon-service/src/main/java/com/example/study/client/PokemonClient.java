package com.example.study.client;

import com.example.study.exception.NotFoundException;
import com.example.study.model.PokemonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class PokemonClient {
  
  public PokemonModel search(final Integer id) {
    
    try {
      var url = String.format("https://pokeapi.co/api/v2/pokemon/%s", id);
      return new RestTemplate().getForObject(url, PokemonModel.class);
    } catch (Exception ex) {
      throw new NotFoundException();
    }
  }
}
