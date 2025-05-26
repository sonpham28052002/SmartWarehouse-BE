package vn.edu.iuh.fit.smartwarehousebe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
@OpenAPIDefinition(servers = {@Server(url = "/api", description = "Default Server URL")})
@EnableAsync
@EnableScheduling
public class SmartWarehouseBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartWarehouseBeApplication.class, args);
    }

}
