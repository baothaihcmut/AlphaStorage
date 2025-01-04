package vn.anpha.storage.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponseDto<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        ApiResponseDto<Object> response = new ApiResponseDto<Object>();
        response.setSuccess(false);
        response.setMessage(exception.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponseDto<Object>> handleAppException(AppException exception) {

        ErrorCode errorCode = exception.getErrorCode();
        ApiResponseDto<Object> response = new ApiResponseDto<Object>();
        response.setSuccess(false);
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponseDto<Object>> handleMethodArgumentNotValidException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponseDto<Object> response = new ApiResponseDto<Object>();
        response.setSuccess(false);
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ApiResponseDto<Object>> handleMethodArgumentNotValidException(
            HttpRequestMethodNotSupportedException exception) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOW;
        ApiResponseDto<Object> response = new ApiResponseDto<Object>();
        response.setSuccess(false);

        response.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    // Method Not Allowed
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponseDto<Object>> handleRuntimeException(RuntimeException exception) {
        System.out.println(exception);
        ApiResponseDto<Object> response = new ApiResponseDto<Object>();
        response.setSuccess(false);

        response.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    // IllegalStateException
    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiResponseDto<Object>> handleNoResourceFoundException(
            NoResourceFoundException exception) {
        ErrorCode errorCode = ErrorCode.API_NOT_EXIST;
        ApiResponseDto<Object> response = new ApiResponseDto<Object>();
        response.setSuccess(false);

        response.setMessage("api not found");
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
}
