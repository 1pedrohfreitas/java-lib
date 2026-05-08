package com.github.javalib.infrastructure.config;

import com.github.javalib.application.service.GoogleAuthApplicationService;
import com.github.javalib.application.service.TelegramBotApplicationService;
import com.github.javalib.domain.port.GoogleOAuthPort;
import com.github.javalib.domain.port.TelegramBotPort;
import com.github.javalib.infrastructure.google.GoogleOAuth2Properties;
import com.github.javalib.services.GoogleAuthDomainService;
import com.github.javalib.services.TelegramDomainService;
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
