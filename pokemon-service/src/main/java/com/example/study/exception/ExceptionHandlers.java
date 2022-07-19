package com.example.study.exception;

import com.example.study.model.ApiExceptionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {
  
  @ExceptionHandler(Exception.class)
  public List<ApiExceptionModel> handleInternalError(Exception error, HttpServletResponse response) {
    log.error("ERRO INTERNO NO SISTEMA: ".concat(error.getMessage()));
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    return List.of(ApiExceptionModel.builder()
                     .code(500L)
                     .message("Erro interno no sistema. Caso o problema persista entre em contato com a central de serviços")
                     .build());
  }
  
  @ExceptionHandler(ApiException.class)
  public ApiExceptionModel handleApiException(ApiException error, HttpServletResponse response) {
    log.error(error.getMessage());
    response.setStatus(error.getHttpStatus().value());
    return ApiExceptionModel.builder()
             .code(error.getCode())
             .message(error.getMessage())
             .build();
  }
  
  @ExceptionHandler(ServletRequestBindingException.class)
  public ApiExceptionModel handleSpringBindValidationException(ServletRequestBindingException error,
                                                               HttpServletResponse response) {
    log.error(error.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    return ApiExceptionModel.builder()
             .code(400L)
             .message(error.getMessage())
             .build();
  }
  
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ApiExceptionModel handleMissingMultipartFieldException(MissingServletRequestPartException error,
                                                                HttpServletResponse response) {
    log.error(error.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    return ApiExceptionModel.builder()
             .code(400L)
             .message(error.getMessage())
             .build();
  }
  
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public List<ApiExceptionModel> handleInvalidBodyException(HttpMessageNotReadableException error,
                                                            HttpServletResponse response) {
    log.error(error.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    Throwable rootCause = error.getRootCause();
    if (rootCause instanceof ApiException apiException) {
      return List.of(ApiExceptionModel.builder()
                       .code(apiException.getCode())
                       .message(apiException.getMessage())
                       .build());
    }
    return List.of(ApiExceptionModel.builder()
                     .code(400L)
                     .message("Verifique o corpo da requisição de acordo com o contrato e refaça a operação")
                     .build());
  }
  
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ApiExceptionModel handleMethodTypeMismatchException(MethodArgumentTypeMismatchException error,
                                                             HttpServletResponse response) {
    log.error(error.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    return ApiExceptionModel.builder()
             .code(400L)
             .message(error.getMessage())
             .build();
  }
}
