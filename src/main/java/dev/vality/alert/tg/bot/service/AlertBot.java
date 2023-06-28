package dev.vality.alert.tg.bot.service;

import dev.vality.alert.tg.bot.config.properties.AlertBotProperties;
import dev.vality.alert.tg.bot.exeptions.AlertTgBotException;
import dev.vality.alert.tg.bot.exeptions.UnknownHandlerException;
import dev.vality.alert.tg.bot.handler.CommonHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static dev.vality.alert.tg.bot.constants.UserStatuses.ALLOWED_USER_STATUSES;
import static dev.vality.alert.tg.bot.utils.UserUtils.getUserId;
import static dev.vality.alert.tg.bot.utils.UserUtils.getUserName;

@Slf4j
@Service
public class AlertBot extends TelegramLongPollingBot {

    private final List<CommonHandler> handlers;
    private final AlertBotProperties alertBotProperties;
    private final MayDayService mayDayService;

    public AlertBot(AlertBotProperties alertBotProperties,
                    List<CommonHandler> handlers,
                    MayDayService mayDayService) throws TelegramApiException {
        super(alertBotProperties.getToken());
        this.handlers = handlers;
        this.alertBotProperties = alertBotProperties;
        this.mayDayService = mayDayService;
        this.execute(new SetMyCommands(List.of(
                new BotCommand("/start", "Main Menu")), new BotCommandScopeDefault(), null));
    }

    @Override
    public String getBotUsername() {
        return alertBotProperties.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (isUserPermission(update)) {
                long userId = getUserId(update);
                var answer = handlers.stream()
                        .filter(handler -> handler.filter(update))
                        .findFirst()
                        .orElseThrow(UnknownHandlerException::new)
                        .handle(update, userId);
                if (answer instanceof SendMessage sendMessage) {
                    sendMessage.setChatId(userId);
                    execute(sendMessage);
                } else if (answer instanceof AnswerInlineQuery answerInlineQuery) {
                    answerInlineQuery.setIsPersonal(true);
                    answerInlineQuery.setCacheTime(0);
                    execute(answerInlineQuery);
                }
            }
        } catch (TelegramApiException | TException ex) {
            throw new AlertTgBotException(String.format("Received an exception while handle update %s", update), ex);
        }
    }

    public boolean isUserPermission(Update update) throws TException {
        Long userId = getUserId(update);
        String userName = getUserName(update);
        try {
            return isChatMemberPermission(userId);
        } catch (TelegramApiException e) {
            checkExceptionIsUserInChat(e, userName, userId);
            throw new AlertTgBotException(String.format("Error while checking user %s permissions", userName), e);
        }
    }

    public boolean isChatMemberPermission(Long userId) throws TelegramApiException, TException {
        ChatMember chatMember = execute(new GetChatMember(alertBotProperties.getChatId(), userId));
        if (ALLOWED_USER_STATUSES.contains(chatMember.getStatus())) {
            return true;
        } else {
            mayDayService.deleteAllAlerts(String.valueOf(userId));
            SendMessage message = new SendMessage();
            message.setChatId(userId);
            message.setText("У вас не прав на создание алертов, все ранее созданные алерты удалены");
            execute(message);
            return false;
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
