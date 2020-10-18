package apricot.workshopsystem.entityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestError handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();

        List<String> errorList = new ArrayList<>();

        result.getFieldErrors().forEach((fieldError) ->
                errorList.add(fieldError.getObjectName() + "." + fieldError.getField() + " : " +
                        fieldError.getDefaultMessage() + " : rejected value [" + fieldError.getRejectedValue() +"]"));

        result.getGlobalErrors().forEach((fieldError) ->
                errorList.add(fieldError.getObjectName() + " : " + fieldError.getDefaultMessage()));

        return new RestError(HttpStatus.BAD_REQUEST, ex.getMessage(), errorList);
    }
}
