package dev.pedrohfreitas.javalib.infrastructure.config;

import dev.pedrohfreitas.javalib.application.service.GoogleAuthApplicationService;
import dev.pedrohfreitas.javalib.application.service.TelegramBotApplicationService;
import dev.pedrohfreitas.javalib.domain.port.GoogleOAuthPort;
import dev.pedrohfreitas.javalib.domain.port.TelegramBotPort;
import dev.pedrohfreitas.javalib.infrastructure.google.GoogleOAuth2Properties;
import dev.pedrohfreitas.javalib.services.GoogleAuthDomainService;
import dev.pedrohfreitas.javalib.services.TelegramDomainService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracao da camada de aplicacao: wiring de services e ports.
 * Beans sao criados apenas quando os modulos correspondentes estao ativos.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public TelegramDomainService telegramDomainService() {
        return new TelegramDomainService();
    }

    @Bean
    @ConditionalOnBean(GoogleOAuth2Properties.class)
    public GoogleAuthDomainService googleAuthDomainService(GoogleOAuth2Properties props) {
        return new GoogleAuthDomainService(props.allowedIssuers(), props.expectedAudience());
    }

    @Bean
    @ConditionalOnBean(TelegramBotPort.class)
    public TelegramBotApplicationService telegramBotApplicationService(
            TelegramDomainService domainService, TelegramBotPort botPort) {
        return new TelegramBotApplicationService(domainService, botPort);
    }

    @Bean
    @ConditionalOnBean({GoogleAuthDomainService.class, GoogleOAuthPort.class})
    public GoogleAuthApplicationService googleAuthApplicationService(
            GoogleAuthDomainService domainService, GoogleOAuthPort oauthPort) {
        return new GoogleAuthApplicationService(domainService, oauthPort);
    }
}
