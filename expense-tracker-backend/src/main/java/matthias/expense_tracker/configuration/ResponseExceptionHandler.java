package matthias.expense_tracker.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    ResponseEntity<Object> handleConflict(EntityExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Conflict (403)", new HttpHeaders(), CONFLICT, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Not Found (404)", new HttpHeaders(), NOT_FOUND, request);
    }
}
