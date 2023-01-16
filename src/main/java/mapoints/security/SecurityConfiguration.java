package mapoints.security;

import mapoints.lib.service.AppConfig;
import mapoints.user.model.UserType;
import mapoints.user.service.BasicUserService;
import mapoints.user.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration  {

    private AuthenticationExceptionHandler authExceptionHandler;
    private BasicUserService basicUserService;
    private JwtService jwtService;

    private final String[] permittedUrls = {
            AppConfig.CUSTOMER_BASE_URL+"/register/**",
            AppConfig.MERCHANT_BASE_URL+"/register/**",
            AppConfig.CUSTOMER_BASE_URL+"/login/**",
            AppConfig.MERCHANT_BASE_URL+"/login/**",
            AppConfig.CUSTOMER_BASE_URL+"/generate_auth_code/**",
            AppConfig.MERCHANT_BASE_URL+"/generate_auth_code/**",
            AppConfig.CUSTOMER_BASE_URL+"/reset_password/**",
            AppConfig.MERCHANT_BASE_URL+"/reset_password/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers(permittedUrls).permitAll()
            .antMatchers(AppConfig.MERCHANT_BASE_URL+"/**").hasAuthority(UserType.MERCHANT.toString())
            .antMatchers(AppConfig.CUSTOMER_BASE_URL+"/**").hasAuthority(UserType.CUSTOMER.toString())
            .antMatchers(AppConfig.ADMIN_BASE_URL+"/**").hasAuthority(UserType.ADMIN.toString())
            .anyRequest().authenticated()
            .and().exceptionHandling().authenticationEntryPoint(authExceptionHandler)
            .and()
            .httpBasic()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(new AuthenticationFilter( jwtService, basicUserService));
        return http.build();
    }

    @Autowired
    public void setAuthExceptionHandler(AuthenticationExceptionHandler authExceptionHandler) {
        this.authExceptionHandler = authExceptionHandler;
    }

    @Autowired
    public void setBasicUserService(BasicUserService basicUserService) {
        this.basicUserService = basicUserService;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
