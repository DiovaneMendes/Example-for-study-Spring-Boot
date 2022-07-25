package com.example.study.service;

import com.example.study.client.PokemonClient;
import com.example.study.model.PokemonModel;
import com.example.study.repository.PokemonRepository;
import com.example.study.stub.MetricsStub;
import com.example.study.stub.PokemonModelStub;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@DisplayName("PokemonService")
@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
public class PokemonServiceTest {
  
  @Mock private PokemonClient client;
  @Mock private MeterRegistry meterRegistry;
  @Mock private PokemonRepository repository;
  @Mock private Counter cacheCounterTest = MetricsStub.counterStub();
  @Mock private Counter errorCounterTest = MetricsStub.counterStub();
  @Mock private Counter onlineCounterTest = MetricsStub.counterStub();
  @InjectMocks private PokemonService pokemonService;
  
  private static final PokemonModel STUB = PokemonModelStub.stub();
  private static final Integer ID = 1;
  
  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(pokemonService, "cacheCounter", cacheCounterTest);
    ReflectionTestUtils.setField(pokemonService, "errorCounter", errorCounterTest);
    ReflectionTestUtils.setField(pokemonService, "onlineCounter", onlineCounterTest);
    doNothing().when(cacheCounterTest).increment();
    doNothing().when(onlineCounterTest).increment();
  }
  
  @Nested
  @DisplayName("Dado que o método [search] seja chamado...")
  class ChamadaSearch {
    
    @Nested
    @DisplayName("Dado que o id passado tenha dados em cache...")
    class IdComDadosEmCache {
      
      private PokemonModel result;
      
      @BeforeEach
      void setup() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(STUB));
        result = pokemonService.search(ID);
      }
  
      @Test
      @DisplayName("Deve disparar log: Searching in cache...")
      void deveDisparaLogSearchingInCache(CapturedOutput capturedOutput) {
        assertTrue(capturedOutput.getOut().contains("Searching in cache..."));
      }
  
      @Test
      @DisplayName("Deve chamar método [findById] do PokemonRepository")
      void deveChamarFindById_PokemonRepository() {
        verify(repository).findById(ID);
      }
      
      @Test
      @DisplayName("Deve chamar método [increment] do cacheCounter")
      void deveChamarIncrement_cacheCounter() {
        verify(cacheCounterTest).increment();
      }
      
      @Test
      @DisplayName("Não deve chamar o método [searchById] do PokemonClient")
      void naoDeveChamarSearchById_PokemonClient(){
        verify(client, never()).searchById(anyInt(), any());
      }
  
      @Test
      @DisplayName("Não deve chamar método [increment] do cacheCounter")
      void naoDeveChamarIncrement_cacheCounter() {
        verify(onlineCounterTest, never()).increment();
      }
  
      @Test
      @DisplayName("Não deve chamar o método [save] do PokemonRepository")
      void naoDeveChamarSave_PokemonRepository(){
        verify(repository, never()).save(any());
      }
      
      @Test
      @DisplayName("Deve retornar um PokemonModel")
      void deveRetornarPokemonModel() {
        assertEquals(STUB, result);
      }
    }
  
    @Nested
    @DisplayName("Dado que o id passado não tenha dados em cache...")
    class IdSemDadosEmCache {
    
      private PokemonModel result;
    
      @BeforeEach
      void setup() {
        when(repository.save(any())).thenReturn(STUB);
        when(client.searchById(anyInt(), any())).thenReturn(STUB);
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        result = pokemonService.search(ID);
      }
  
      @Test
      @DisplayName("Deve chamar método [findById] do PokemonRepository")
      void deveChamarFindById_PokemonRepository() {
        verify(repository).findById(ID);
      }
    
      @Test
      @DisplayName("Deve disparar log: Searching online...")
      void deveDisparaLogSearchingOnline(CapturedOutput capturedOutput) {
        assertTrue(capturedOutput.getOut().contains("Searching online..."));
      }
    
      @Test
      @DisplayName("Não deve chamar método [increment] do cacheCounter")
      void naoDeveChamarIncrement_cacheCounter() {
        verify(cacheCounterTest, never()).increment();
      }
    
      @Test
      @DisplayName("Deve chamar o método [searchById] do PokemonClient")
      void deveChamarSearchById_PokemonClient(){
        verify(client).searchById(ID, errorCounterTest);
      }
    
      @Test
      @DisplayName("Deve chamar método [increment] do onlineCounterTest")
      void deveChamarIncrement_onlineCounterTest() {
        verify(onlineCounterTest).increment();
      }
    
      @Test
      @DisplayName("Deve chamar o método [save] do PokemonRepository")
      void deveChamarSave_PokemonRepository(){
        verify(repository).save(STUB);
      }
    
      @Test
      @DisplayName("Deve retornar um PokemonModel")
      void deveRetornarPokemonModel() {
        assertEquals(STUB, result);
      }
    }
  }
}
