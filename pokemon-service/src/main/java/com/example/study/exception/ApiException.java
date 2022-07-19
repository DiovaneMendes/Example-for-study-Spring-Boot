package com.example.study.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException{
  
  public ApiException() { }
  
  public ApiException(String mensagem) {
    super(mensagem);
  }
  
  public abstract Long getCode();
  
  public abstract String getMessage();
  
  public abstract HttpStatus getHttpStatus();
}