package vn.edu.iuh.fit.smartwarehousebe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Configuration

public class NormalConfig {
        @Bean(name = "customHandlerExceptionResolver")
        @Primary
        public HandlerExceptionResolver handlerExceptionResolver() {
            return new ExceptionHandlerExceptionResolver();
        }
}
