package vn.edu.iuh.fit.smartwarehousebe.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  private SentryInterceptor sentryInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(sentryInterceptor);
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    for (HttpMessageConverter<?> converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter jsonConverter) {
        List<MediaType> supportedMediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "json", StandardCharsets.UTF_8));
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
      }
    }
  }
}
