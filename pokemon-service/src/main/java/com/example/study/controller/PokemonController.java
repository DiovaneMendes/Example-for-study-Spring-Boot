package com.example.study.controller;

import com.example.study.exception.NotFoundException;
import com.example.study.model.PokemonModel;
import com.example.study.service.PokemonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/search")
@Api("Endpoints de pesquisa versão 1")
public class PokemonController {
  
  @Autowired
  private PokemonService pokemonService;
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Pesquisa pokémon pelo id")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Pesquisa realizada com sucesso", response = PokemonModel.class),
    @ApiResponse(code = 404, message = "Pokémon com id passado não existe!", response = NotFoundException.class)
  })
  public PokemonModel searchById(@PathVariable Integer id) {
    return pokemonService.search(id);
  }
}
