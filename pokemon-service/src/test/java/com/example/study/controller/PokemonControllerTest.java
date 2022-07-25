package com.example.study.controller;

import com.example.study.exception.NotFoundException;
import com.example.study.service.PokemonService;
import com.example.study.stub.PokemonModelStub;
import com.example.study.stub.StringStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("PokemonController")
@ExtendWith(SpringExtension.class)
public class PokemonControllerTest {
  
  @Autowired private MockMvc mockMvc;
  @MockBean private PokemonService pokemonService;
  
  @Nested
  @DisplayName("Dado que a rota do método [searchById] seja chamada...")
  class ChamadaSearchById {
    
    @Nested
    @DisplayName("Dado que o id passado seja válido...")
    class IdValido {
      
      @Test
      @DisplayName("Deve retornar um PokemonModel")
      void deveRetornarPokemonModel() throws Exception {
        final var expected = StringStub.resultSuccessController();
        
        when(pokemonService.search(anyInt())).thenReturn(PokemonModelStub.stub());
        
        var mvcResult = mockMvc
                          .perform(get("/v1/search/1"))
                          .andExpect(status().isOk())
                          .andReturn();
        
        assertEquals(expected, mvcResult.getResponse().getContentAsString());
        
        verify(pokemonService).search(1);
      }
    }
  
    @Nested
    @DisplayName("Dado que o id passado seja inválido...")
    class IdInvalido {
    
      @Test
      @DisplayName("Deve retornar statusCode 404 e mensagem: Pokemon with past id doesn't exist!")
      void deveRetornarErro() throws Exception {
        final var expected = StringStub.resultErrorController();
      
        when(pokemonService.search(anyInt())).thenThrow(new NotFoundException());
      
        var mvcResult = mockMvc
                          .perform(get("/v1/search/3479"))
                          .andExpect(status().isNotFound())
                          .andReturn();
      
        assertEquals(expected, mvcResult.getResponse().getContentAsString());
        
        verify(pokemonService).search(3479);
      }
    }
    
  }
}
