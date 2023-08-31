package ru.practicum.exploreWithMe.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exploreWithMe.exception.BadRequestException;
import ru.practicum.exploreWithMe.exception.ConflictException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.ApiError;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(NotFoundException exception) {
        return ApiError.builder()
                .message("The required object was not found.")
                .reason(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(BadRequestException exception) {
        return ApiError.builder()
                .message("Incorrectly made request.")
                .reason(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(ConflictException exception) {
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(exception.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNumberFormat(NumberFormatException exception) {
        return ApiError.builder()
                .message("Incorrectly made request.")
                .reason(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolation(ConstraintViolationException exception) {
        return ApiError.builder()
                .message("Integrity constraint has been violated.")
                .reason(exception.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return ApiError.builder()
                .message("Incorrectly made request.")
                .reason(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePSQLException(SQLException exception) {
        return ApiError.builder()
                .message("Integrity constraint has been violated.")
                .reason(exception.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ApiError.builder()
                .message("Integrity constraint has been violated.")
                .reason(exception.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
