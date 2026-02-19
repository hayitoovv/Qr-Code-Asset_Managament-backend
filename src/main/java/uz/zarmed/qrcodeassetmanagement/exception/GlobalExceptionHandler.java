package uz.zarmed.qrcodeassetmanagement.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(DepartmentNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
    @ExceptionHandler(EmailAlredyExistException.class)
    public ResponseEntity<String> handleEmailAlredyExist(EmailAlredyExistException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }
    @ExceptionHandler(DepartmentCodeAlredyExistException.class)
    public ResponseEntity<String> handleDepartmentCodeAlredyExist(DepartmentCodeAlredyExistException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(400).body(ex.getBindingResult().getFieldError().getDefaultMessage());
    }
}