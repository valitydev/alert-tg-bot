package dev.vality.alert.tg.bot.utils;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@RequiredArgsConstructor
public class UserUtils {

    public static long getUserId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getFrom().getId()
                : update.hasCallbackQuery()
                ? update.getCallbackQuery().getFrom().getId()
                : update.hasInlineQuery()
                ? update.getInlineQuery().getFrom().getId()
                : update.getMyChatMember().getFrom().getId();
    }

    public static String getUserName(Update update) {
        return update.hasMessage()
                ? update.getMessage().getFrom().getUserName()
                : update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage().getFrom().getUserName()
                : update.hasInlineQuery()
                ? update.getInlineQuery().getFrom().getUserName()
                : update.getMyChatMember().getFrom().getUserName();
    }

    public static boolean isUserInBot(Update update) {
        return Objects.equals(update.getMessage().getChatId(), update.getMessage().getFrom().getId());
    }
}
