package oficioya_notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("API del micro servicio Notification de la aplicacion Oficio Ya")
                .version("1.0.0")
                .description("Documentación interactiva con Swagger UI para el CRUD de las notificaciones"));
    }
}