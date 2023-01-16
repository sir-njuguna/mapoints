package mapoints.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import mapoints.lib.service.Message;
import mapoints.lib.view.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse res,
                         AuthenticationException authEx)
            throws IOException {
        String languageCode = req.getHeader("Accept-Language");
        if(languageCode == null){
            languageCode = "en";
        }
        ApiResponse apiResponse = new ApiResponse(
            false,
            HttpStatus.UNAUTHORIZED.value(),
                Message.get("auth.invalid", new Locale(languageCode))
        );
        apiResponse.setTime(new Date());
        ObjectMapper mapper = new ObjectMapper();
        String responseMsg = mapper.writeValueAsString(apiResponse);
        res.getWriter().write(responseMsg);
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
   }
}
