package com.example.study.client;

import com.example.study.exception.NotFoundException;
import com.example.study.model.PokemonModel;
import com.example.study.stub.MetricsStub;
import com.example.study.stub.PokemonModelStub;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("PokemonClient")
@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
public class PokemonClientTest {
  
  @Mock private RestTemplate restTemplate = new RestTemplate();
  @InjectMocks private PokemonClient pokemonClient;
  
  private final Counter ERROR_COUNTER = MetricsStub.counterStub();
  
  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(pokemonClient, "restTemplate", restTemplate);
  }
  
  @Nested
  @DisplayName("Dado que o método [searchById] seja chamado...")
  class ChamadaSearchById {
  
    @Nested
    @DisplayName("Dado que o id passado seja válido...")
    class IdValido {
      
      private PokemonModel expected, result;
      
      @BeforeEach
      void setup() {
        expected = PokemonModelStub.stub();
        when(restTemplate.getForObject(anyString(), any())).thenReturn(expected);
        result = pokemonClient.searchById(1, ERROR_COUNTER);
      }
      
      @Test
      @DisplayName("Deve chamar o método [getForObject] do RestTemplate")
      void deveChamarMetodoGetForObject() {
        verify(restTemplate).getForObject("https://pokeapi.co/api/v2/pokemon/1", PokemonModel.class);
      }
      
      @Test
      @DisplayName("Deve retornar um PokemonModel")
      void deveRetornarPokemonModel() {
        assertEquals(result, expected);
      }
    }
  
    @Nested
    @DisplayName("Dado que o id passado seja inválido...")
    class IdInvalido {
    
      private Integer idError;
      
      @BeforeEach
      void setup() {
        when(restTemplate.getForObject(anyString(), any())).thenThrow(new RestClientException("ERRO"));
        idError = 78561;
      }
      
      @Test
      @DisplayName("Deve chamar o método [getForObject] do RestTemplate")
      void deveChamarMetodoGetForObject() {
        assertThrows(NotFoundException.class, () -> pokemonClient.searchById(78561, ERROR_COUNTER));
        verify(restTemplate).getForObject("https://pokeapi.co/api/v2/pokemon/78561", PokemonModel.class);
      }
  
      @Test
      @DisplayName("Deve disparar log: Error searching for id: 78561")
      void deveDisparaLogErrorSearching(CapturedOutput capturedOutput) {
        assertThrows(NotFoundException.class, () -> pokemonClient.searchById(78561, ERROR_COUNTER));
        assertTrue(capturedOutput.getOut().contains("Error searching for id: 78561"));
      }
      
      @Test
      @DisplayName("Deve retornar um NotFoundException")
      void deveRetornarNotFoundException() {
        assertThatThrownBy(() -> pokemonClient.searchById(idError, ERROR_COUNTER))
          .isInstanceOf(NotFoundException.class)
          .hasMessage("Pokemon with past id doesn't exist!");
      }
    }
  }
}
