package me.blog.anabada.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class AnabadaExceptionHandler {

    private static final String ERROR_LOG_MSG = "[REQUEST (Method: %s) (URI: %s)] [ERROR (Type: %s) (Message: %s)]";

    @ExceptionHandler({AnabadaServiceException.class})
    public void serviceException(HttpServletRequest request, AnabadaServiceException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }

    private String getExceptionLog(HttpServletRequest request, Exception exception) {
        return String.format(
            ERROR_LOG_MSG,
            request.getMethod(),
            request.getRequestURI(),
            exception.getClass().getSimpleName(),
            exception.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public void httpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public void httpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public void methodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MissingPathVariableException.class})
    public void missingServletRequestParameterException(HttpServletRequest request, MissingRequestValueException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public void constraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }

    @ExceptionHandler({BindException.class})
    public void bindException(HttpServletRequest request, BindException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public void httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public void methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }

    @ExceptionHandler({Exception.class})
    public void unknownException(HttpServletRequest request, Exception ex) {
        log.error(getExceptionLog(request, ex), ex);

        return;
    }
}
