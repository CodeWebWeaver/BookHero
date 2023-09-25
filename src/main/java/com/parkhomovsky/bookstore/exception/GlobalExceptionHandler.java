package com.parkhomovsky.bookstore.exception;

import com.parkhomovsky.bookstore.dto.exception.ErrorResponseDto;
import com.parkhomovsky.bookstore.dto.exception.ErrorResponseListDto;
import io.jsonwebtoken.JwtException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        ErrorResponseListDto errorResponse = new ErrorResponseListDto();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        errorResponse.setValidationErrors(errors);
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleUniqueDataDuplicate(
            DataIntegrityViolationException ex) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody("An error occurred while processing the request. "
                        + "You are attempting to add an item "
                        + "that violates a unique constraint in the database: " + ex.getMessage(),
                        HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({RegistrationException.class})
    protected ResponseEntity<ErrorResponseDto> handleRegistrationException(
            RegistrationException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody("Entered wrong data for registration: "
                                + ex.getMessage(),
                        HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponseDto> handleAccessDeniedException(
            AccessDeniedException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody("User does not have access for this action: "
                                + ex.getMessage(),
                        HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponseDto> handleJwtException(JwtException ex) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody("User not authenticated: Error during proceeding JWT: "
                        + ex.getMessage(),
                        HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponseDto> handleBadCredentialsException(
            BadCredentialsException ex) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody("User not authenticated: Wrong data entered: "
                        + ex.getMessage(),
                        HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidRequestParametersException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRequestParametersException(
            InvalidRequestParametersException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody("An error occurred while proceeding request data: "
                        + ex.getMessage(),
                        HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(
            EntityNotFoundException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody("An error occurred while proceeding data from database. "
                        + "Nothing found by provided parameters: "
                        + ex.getMessage(),
                        HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyShoppingCartException.class)
    public ResponseEntity<ErrorResponseDto> handleEmptyShoppingCartException(
            EmptyShoppingCartException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody("An error occurred while proceeding shopping cart data: "
                        + ex.getMessage(),
                        HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private ErrorResponseDto getErrorMessageBody(String errorMessage, HttpStatus httpStatus) {
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(httpStatus);
        errorResponse.setError(errorMessage);
        return errorResponse;
    }

    private String getErrorMessage(ObjectError ex) {
        if (ex instanceof FieldError) {
            String field = ((FieldError) ex).getField();
            String defaultMessage = ex.getDefaultMessage();
            return defaultMessage + " for " + field;
        }
        return ex.getDefaultMessage();
    }
}
