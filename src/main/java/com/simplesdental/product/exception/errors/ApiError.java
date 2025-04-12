package com.simplesdental.product.exception.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private final LocalDateTime timestamp;
  private final int status;
  private final String error;
  private final String message;
  private final String path;
  private final List<String> details;

  private ApiError(Builder builder) {
    this.timestamp = builder.timestamp;
    this.status = builder.status;
    this.error = builder.error;
    this.message = builder.message;
    this.path = builder.path;
    this.details = builder.details;
  }

  public static class Builder {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> details;

    public Builder timestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder status(int status) {
      this.status = status;
      return this;
    }

    public Builder error(String error) {
      this.error = error;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder details(List<String> details) {
      this.details = details;
      return this;
    }

    public ApiError build() {
      return new ApiError(this);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

}