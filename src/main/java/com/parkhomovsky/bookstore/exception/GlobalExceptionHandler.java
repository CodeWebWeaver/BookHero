package com.parkhomovsky.bookstore.exception;

import com.parkhomovsky.bookstore.dto.exception.ErrorResponseDto;
import com.parkhomovsky.bookstore.dto.exception.ErrorResponseListDto;
import io.jsonwebtoken.JwtException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NonNull;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
        errorResponse.setError(errors);
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleUniqueDataDuplicate(DataIntegrityViolationException ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String errorMessage = "An error occurred while processing the request. "
                + "You are attempting to add an item "
                + "that violates a unique constraint in the database";
        ErrorResponseDto errorResponse =
                getErrorMessageBody(errorMessage, rootCause, HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({RegistrationException.class})
    protected ResponseEntity<Object> handleRegistrationException(
            RegistrationException ex
    ) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String errorMessage = "Entered wrong data for registration";
        ErrorResponseDto errorResponse =
                getErrorMessageBody(errorMessage, rootCause, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex
    ) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String errorMessage = "User does not have access for this action";
        ErrorResponseDto errorResponse =
                getErrorMessageBody(errorMessage, rootCause, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<Object> handleJwtException(JwtException ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String errorMessage = "User not authenticated: Error during proceeding JWT";
        ErrorResponseDto errorResponse =
                getErrorMessageBody(errorMessage, rootCause, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String errorMessage = "User not authenticated: Wrong data entered";
        ErrorResponseDto errorResponse =
                getErrorMessageBody(errorMessage, rootCause, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidRequestParametersException.class)
    public ResponseEntity<Object> handleInvalidRequestParametersException(
            InvalidRequestParametersException ex
    ) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String errorMessage = "An error occurred while proceeding request data.";
        ErrorResponseDto errorResponse =
                getErrorMessageBody(errorMessage, rootCause, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex
    ) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String errorMessage = "An error occurred while proceeding data from database. "
                + "Nothing found by provided parameters";
        ErrorResponseDto errorResponse =
                getErrorMessageBody(errorMessage, rootCause, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private ErrorResponseDto getErrorMessageBody(String errorMessage,
                                                    Throwable rootCause,
                                                    HttpStatus httpStatus) {
        if (rootCause != null) {
            errorMessage = "Error: " + rootCause.getMessage();
        }
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.CONFLICT);
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
