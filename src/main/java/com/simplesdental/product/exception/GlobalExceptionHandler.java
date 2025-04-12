package com.simplesdental.product.exception;

import com.simplesdental.product.exception.custom.BusinessException;
import com.simplesdental.product.exception.custom.EmptyResultException;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.exception.custom.ValidationException;
import com.simplesdental.product.exception.errors.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationErrors(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    String message = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse("Erro de validação");

    ApiError response = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(message)
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.badRequest().body(response);
  }


  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiError> handleValidationException(
      ValidationException ex,
      HttpServletRequest request
  ) {
    ApiError response = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(EmptyResultException.class)
  public ResponseEntity<ApiError> handleEmptyResult(EmptyResultException ex,
      HttpServletRequest request) {
    ApiError response = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NO_CONTENT.value())
        .error("No Content")
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
  }

  @ExceptionHandler(ModelNotFoundException.class)
  public ResponseEntity<ApiError> handleModelNotFound(
      ModelNotFoundException ex,
      HttpServletRequest request
  ) {
    ApiError response = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiError> handleBusinessException(
      BusinessException ex,
      HttpServletRequest request
  ) {
    ApiError response = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
        .error("Erro de regra de negocio")
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.unprocessableEntity().body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleInternalErrors(
      Exception ex,
      HttpServletRequest request
  ) {
    ApiError response = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error("Erro interno")
        .message("Ocorreu um erro inesperado")
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.internalServerError().body(response);
  }
}