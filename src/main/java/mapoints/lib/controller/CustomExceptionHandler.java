package mapoints.lib.controller;


import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.service.Message;
import mapoints.lib.view.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler({CommonRuntimeException.class})
    public ApiResponse handleErrors(CommonRuntimeException exp, Locale locale) {
        return new ApiResponse(false,
                exp.getType().value(),
                Message.get(exp.getMessage(), locale)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse HandleInvalidArgumentsException(MethodArgumentNotValidException exp, Locale locale) {
        List<ObjectError> errorCodes = exp.getBindingResult().getAllErrors();
        List<String> errors = new ArrayList<>();
        errorCodes.forEach(objectError -> {
            String defaultErrorMsg = objectError.getDefaultMessage();
            if (defaultErrorMsg != null) {
                errors.add(Message.get(defaultErrorMsg, locale));
            }
        });

        return new ApiResponse(false,
                HttpStatus.BAD_REQUEST.value(),
                String.join("\n", errors)
        );
    }
}
