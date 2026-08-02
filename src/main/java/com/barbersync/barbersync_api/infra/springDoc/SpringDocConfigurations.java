package com.barbersync.barbersync_api.infra.springDoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("BarberSync API")
                        .version("0.0.1")
                        .description("API/Backend do projeto BarberSync, plataforma de controle e gestão de uma barbearia, com fluxo de agendamentos, controle de clientes e serviços e gestão financeira.")
                );
    }
}
