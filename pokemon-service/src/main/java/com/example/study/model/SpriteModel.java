package com.example.study.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class SpriteModel {

  @JsonProperty("front_default")
  @ApiModelProperty(value = "frontDefault", example = "https://exemplo/PokeAPI/sprites/master/sprites/pokemon/1.png")
  private String frontDefault;
}
