package dev.pedrohfreitas.javalib.entrypoint.controller;

import dev.pedrohfreitas.javalib.application.service.TelegramBotApplicationService;
import dev.pedrohfreitas.javalib.domain.model.TelegramMessage;
import dev.pedrohfreitas.javalib.dto.SendMessageRequest;
import dev.pedrohfreitas.javalib.entrypoint.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints do Telegram Bot.
 * Disponivel apenas quando o modulo Telegram esta configurado.
 */
@RestController
@RequestMapping("/api/v1/bot")
@RequiredArgsConstructor
@ConditionalOnBean(TelegramBotApplicationService.class)
public class BotController {

    private final TelegramBotApplicationService botService;

    @PostMapping("/send")
    public ApiResponse<String> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        var message = new TelegramMessage(request.chatId(), request.text(), request.parseMode());
        botService.sendMessage(message);
        return ApiResponse.ok("Message sent");
    }

    @GetMapping("/me")
    public ApiResponse<?> getMe() {
        var botInfo = botService.getBotInfo();
        return ApiResponse.ok(botInfo);
    }
}
