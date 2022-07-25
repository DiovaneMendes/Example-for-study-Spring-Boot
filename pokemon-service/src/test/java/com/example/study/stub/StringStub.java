package com.example.study.stub;

public class StringStub {
  
  public static String resultSuccessController() {
    return "{\"id\":1,\"name\":\"charmeleon\",\"weight\":\"190\",\"sprites\":{\"front_default\":\"https://example/PokeAPI/sprites/master/sprites/pokemon/1.png\"}}";
      
  }
  
  public static String resultErrorController() {
    return "{\"code\":404,\"message\":\"Pokemon with past id doesn't exist!\"}";
  }
  
}
