package org.acacho.datastaxs3.adapter.in;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acacho.datastaxs3.domain.exception.AssetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class AssetErrorAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler({AssetNotFoundException.class})
  public void handle(AssetNotFoundException e, HttpServletResponse response) throws IOException {
    log.error(e.getMessage());
    response.sendError(HttpStatus.NOT_FOUND.value());
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public void handle(IllegalArgumentException e, HttpServletResponse response) throws IOException {
    log.error(e.getMessage());
    response.sendError(HttpStatus.BAD_REQUEST.value());
  }

  @ExceptionHandler({Exception.class})
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public void handle(Exception e) {
    log.error(e.getMessage());
  }
}
