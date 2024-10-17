package vn.anpha.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ApiResponseDto response = new ApiResponseDto();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.setMessage(exception.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponseDto> handleAppException(AppException exception) {

        ErrorCode errorCode = exception.getErrorCode();
        ApiResponseDto response = new ApiResponseDto();
        response.setStatusCode(errorCode.getStatusCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponseDto> handleMethodArgumentNotValidException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        ApiResponseDto response = new ApiResponseDto();
        response.setStatusCode(errorCode.getStatusCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponseDto> handleRuntimeException(RuntimeException exception) {
        ApiResponseDto response = new ApiResponseDto();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
