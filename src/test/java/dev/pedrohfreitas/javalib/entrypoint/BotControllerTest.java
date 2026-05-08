package dev.pedrohfreitas.javalib.entrypoint;

import dev.pedrohfreitas.javalib.application.service.TelegramBotApplicationService;
import dev.pedrohfreitas.javalib.domain.model.TelegramBotInfo;
import dev.pedrohfreitas.javalib.domain.model.TelegramMessage;
import dev.pedrohfreitas.javalib.entrypoint.controller.BotController;
import dev.pedrohfreitas.javalib.entrypoint.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BotControllerTest {

    private MockMvc mockMvc;
    private TelegramBotApplicationService botService;

    @BeforeEach
    void setUp() {
        botService = mock(TelegramBotApplicationService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new BotController(botService))
                .setValidator(new LocalValidatorFactoryBean())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("should send message successfully")
    void shouldSendMessageSuccessfully() throws Exception {
        when(botService.sendMessage(any())).thenReturn(true);

        mockMvc.perform(post("/api/v1/bot/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"chatId":"123","text":"Hello"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("should return bad request when chatId is blank")
    void shouldReturnBadRequestWhenChatIdIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/bot/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"chatId":"","text":"Hello"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return bot info")
    void shouldReturnBotInfo() throws Exception {
        var botInfo = new TelegramBotInfo(456L, "test_bot");
        when(botService.getBotInfo()).thenReturn(botInfo);

        mockMvc.perform(get("/api/v1/bot/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("test_bot"));
    }
}
