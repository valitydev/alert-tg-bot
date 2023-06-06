package dev.vality.alert.tg.bot.service;

import dev.vality.alert.tg.bot.config.properties.AlertBotProperties;
import dev.vality.alert.tg.bot.exeptions.AlertTgBotException;
import dev.vality.alert.tg.bot.handler.CommonHandler;
import dev.vality.alert.tg.bot.handler.MainMenuHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static dev.vality.alert.tg.bot.constants.UserStatuses.ALLOWED_USER_STATUSES;

@Slf4j
@Service
public class AlertBot extends TelegramLongPollingBot {

    private final List<CommonHandler> handlers;
    private final AlertBotProperties alertBotProperties;

    public AlertBot(AlertBotProperties alertBotProperties, List<CommonHandler> handlers) {
        super(alertBotProperties.getToken());
        this.handlers = handlers;
        this.alertBotProperties = alertBotProperties;
    }

    @Override
    public String getBotUsername() {
        return alertBotProperties.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            long userId = update.hasMessage()
                    ? update.getMessage().getFrom().getId()
                    : update.getCallbackQuery().getFrom().getId();
            SendMessage message = handlers.stream()
                    .filter(handler -> handler.filter(update))
                    .findFirst()
                    .orElse(new MainMenuHandler())
                    .handle(update, userId);
            if (message != null && isUserPermission(update)) {
                message.setChatId(userId);
                execute(message);
            }
        } catch (TelegramApiException | TException ex) {
            throw new AlertTgBotException(String.format("Received an exception while handle update %s", update), ex);
        }
    }

    public boolean isUserPermission(Update update) {
        Long userId = update.hasMessage()
                ? update.getMessage().getFrom().getId()
                : update.getCallbackQuery().getMessage().getFrom().getId();
        String userName = update.hasMessage()
                ? update.getMessage().getFrom().getUserName()
                : update.getCallbackQuery().getMessage().getFrom().getUserName();
        try {
            ChatMember chatMember = execute(new GetChatMember(alertBotProperties.getChatId(), userId));
            return ALLOWED_USER_STATUSES.contains(chatMember.getStatus());
        } catch (TelegramApiException e) {
            checkExceptionIsUserInChat(e, userName, userId);
            throw new AlertTgBotException(String.format("Error while checking user %s permissions", userName), e);
        }
    }

    private void checkExceptionIsUserInChat(TelegramApiException e, String userName, Long userId) {
        if (e.getMessage().contains("[400] Bad Request: user not found")) {
            log.info("User {} not found in chat", userName);
            SendMessage message = new SendMessage();
            message.setChatId(userId);
            message.setText("У вас не прав на создание алертов");
            try {
                execute(message);
                throw new AlertTgBotException(String.format("User %s don't have permissions", userName), e);
            } catch (TelegramApiException ex) {
                throw new AlertTgBotException(
                        String.format("Error while checking user %s permissions", userName), e);
            }
        }
    }
}
