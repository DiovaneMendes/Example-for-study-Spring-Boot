package com.example.study.stub;

import com.example.study.model.PokemonModel;
import com.example.study.model.SpriteModel;

public class PokemonModelStub {
  
  public static PokemonModel stub() {
    return PokemonModel.builder()
             .id(1)
             .name("charmeleon")
             .weight("190")
             .sprites(SpriteModel.builder()
                        .frontDefault("https://example/PokeAPI/sprites/master/sprites/pokemon/1.png")
                        .build())
             .build();
  }
}
