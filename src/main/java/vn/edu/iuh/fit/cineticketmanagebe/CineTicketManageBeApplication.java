package vn.edu.iuh.fit.cineticketmanagebe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
@OpenAPIDefinition(servers = {@Server(url = "/api", description = "Default Server URL")})
@EnableAsync
public class CineTicketManageBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CineTicketManageBeApplication.class, args);
    }

}
