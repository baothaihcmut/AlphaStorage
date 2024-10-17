package vn.anpha.storage.exception.ResponseDto;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {

    private Boolean success = true;
    private HttpStatusCode statusCode;
    private String message;
    private T result;

}
