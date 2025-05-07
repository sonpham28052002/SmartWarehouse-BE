package vn.edu.iuh.fit.smartwarehousebe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * @description
 * @author: vie
 * @date: 16/3/25
 */
@Configuration
public class FreemarkerConfig {

  @Bean
  @Primary
  public FreeMarkerConfigurationFactoryBean freemarkerConfiguration() {
    FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
    bean.setTemplateLoaderPath("classpath:/templates/");
    bean.setDefaultEncoding("UTF-8");
    return bean;
  }
}