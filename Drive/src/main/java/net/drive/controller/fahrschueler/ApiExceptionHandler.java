package net.drive.controller.fahrschueler;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
    String msg = ex.getMessage() == null ? "" : ex.getMessage();

    if (msg.contains("vorhanden")) {
      return ResponseEntity.status(409).body(Map.of("error", msg));
    }
    return ResponseEntity.badRequest().body(Map.of("error", msg));
  }
}
