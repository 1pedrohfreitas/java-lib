package dev.pedrohfreitas.javalib.application;

import dev.pedrohfreitas.javalib.application.service.TelegramBotApplicationService;
import dev.pedrohfreitas.javalib.domain.model.TelegramBotInfo;
import dev.pedrohfreitas.javalib.domain.model.TelegramMessage;
import dev.pedrohfreitas.javalib.domain.port.TelegramBotPort;
import dev.pedrohfreitas.javalib.services.TelegramDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotApplicationServiceTest {

    @Mock
    private TelegramBotPort botPort;

    private TelegramBotApplicationService service;

    @BeforeEach
    void setUp() {
        var domainService = new TelegramDomainService();
        service = new TelegramBotApplicationService(domainService, botPort);
    }

    @Test
    @DisplayName("should send message successfully")
    void shouldSendMessageSuccessfully() {
        var message = new TelegramMessage("123", "Hello");
        when(botPort.sendMessage(any())).thenReturn(true);

        boolean result = service.sendMessage(message);

        assertThat(result).isTrue();
        verify(botPort).sendMessage(message);
    }

    @Test
    @DisplayName("should split long message and send all chunks")
    void shouldSplitLongMessageAndSendAllChunks() {
        var longText = "A".repeat(5000);
        var message = new TelegramMessage("123", longText);
        when(botPort.sendMessage(any())).thenReturn(true);

        boolean result = service.sendMessage(message);

        assertThat(result).isTrue();
        verify(botPort, org.mockito.Mockito.times(2)).sendMessage(any());
    }

    @Test
    @DisplayName("should get bot info")
    void shouldGetBotInfo() {
        var expectedInfo = new TelegramBotInfo(456L, "test_bot");
        when(botPort.getMe()).thenReturn(expectedInfo);

        TelegramBotInfo result = service.getBotInfo();

        assertThat(result).isEqualTo(expectedInfo);
        verify(botPort).getMe();
    }
}
