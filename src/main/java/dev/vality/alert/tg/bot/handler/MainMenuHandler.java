package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.constants.TextConstants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

import static dev.vality.alert.tg.bot.utils.MainMenuBuilder.buildMainInlineKeyboardMarkup;

@Component
public class MainMenuHandler implements CommonHandler {

    @Override
    public boolean filter(Update update) {
        return false;
    }

    @Override
    public SendMessage handle(Update update, long userId) {
        if (Objects.equals(update.getMessage().getChatId(), update.getMessage().getFrom().getId())) {
            SendMessage message = new SendMessage();
            message.setChatId(userId);
            message.setText(TextConstants.REPLY_OR_ACTION.getText());
            message.setReplyMarkup(buildMainInlineKeyboardMarkup());
            return message;
        }
        return null;
    }
}
