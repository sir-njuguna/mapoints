package mapoints.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import mapoints.lib.service.AppConfig;
import mapoints.user.model.User;
import mapoints.user.model.UserType;
import mapoints.user.service.BasicUserService;
import mapoints.user.service.JwtService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

class AuthenticationFilter extends BasicAuthenticationFilter {
    private final JwtService jwtService;
    private final BasicUserService userService;
    AuthenticationFilter(
                         JwtService jwtService,
                         BasicUserService userService) {
        super(authentication -> authentication);
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String authToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        UserType userType = getUserType(req.getServletPath());
        if (!StringUtils.isBlank(authToken) && userType != null) {
            try {
                DecodedJWT decodedJWT = jwtService.getDecodedJWT(authToken, userType);
                String userEntityId = decodedJWT.getSubject();
                User user = userService.findByEntityId(userEntityId);

                jwtService.validateJwtId(decodedJWT.getId(), user.getRecentAuthId());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userEntityId,
                    null,
                    List.of(new SimpleGrantedAuthority(userType.toString()))
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (JWTVerificationException ignore){}
        }
        chain.doFilter(req, res);
    }

    private UserType getUserType(String url){
        UserType userType = null;
        if(StringUtils.startsWithIgnoreCase(url, AppConfig.ADMIN_BASE_URL)){
            userType =  UserType.ADMIN;
        }else if(StringUtils.startsWithIgnoreCase(url, AppConfig.MERCHANT_BASE_URL)){
            userType = UserType.MERCHANT;
        }else if(StringUtils.startsWithIgnoreCase(url, AppConfig.CUSTOMER_BASE_URL)){
            userType = UserType.CUSTOMER;
        }
        return userType;
    }
}
