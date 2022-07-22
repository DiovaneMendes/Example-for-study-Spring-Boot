package com.example.study.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class PokemonModel {
  @Id
  @ApiModelProperty(value = "id", example = "1")
  private Integer id;
  
  @ApiModelProperty(value = "name", example = "bulbasaur")
  private String name;
  
  @ApiModelProperty(value = "weight", example = "69")
  private String weight;
  
  private SpriteModel sprites;
}
