package mapoints.lib.service;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {
    public static final String ADMIN_BASE_URL = "/admin";
    public static final String MERCHANT_BASE_URL = "/merchant";
    public static final String CUSTOMER_BASE_URL = "/customer";
    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.addBasenames("classpath:msg/messages");
        return source;
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertiesSource() {
        final PropertySourcesPlaceholderConfigurer propertiesConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertiesConfigurer.setIgnoreResourceNotFound(false);

        final List<Resource> resourceLst = new ArrayList<>();

        resourceLst.add(new ClassPathResource("application.properties"));
        resourceLst.add(new FileSystemResource("/opt/mapoints/jwt.properties"));
        resourceLst.add(new FileSystemResource("/opt/mapoints/db.properties"));
        resourceLst.add(new FileSystemResource("/opt/mapoints/sms-credentials.properties"));

        propertiesConfigurer.setLocations(resourceLst.toArray(new Resource[]{}));

        return propertiesConfigurer;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
