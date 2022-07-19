package com.example.study.controller;

import com.example.study.model.PokemonModel;
import com.example.study.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PokemonController {
  
  @Autowired
  private PokemonService pokemonService;
  
  @GetMapping("/id/{id}")
  @ResponseStatus(HttpStatus.OK)
  public PokemonModel findById(@PathVariable Integer id) {
    return pokemonService.search(id);
  }
}
