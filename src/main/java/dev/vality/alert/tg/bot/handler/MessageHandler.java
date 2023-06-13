package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.constants.TextConstants;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

import static dev.vality.alert.tg.bot.utils.MainMenuBuilder.buildMainInlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MessageHandler implements CommonHandler {

    private final StateDataDao stateDataDao;

    @Override
    public boolean filter(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getReplyToMessage() == null
                && Objects.equals(update.getMessage().getChatId(), update.getMessage().getFrom().getId());
    }

    @Override
    public SendMessage handle(Update update, long userId) {
        SendMessage message = new SendMessage();
        message.setText(TextConstants.SELECT_ACTION.getText());
        StateData stateData = new StateData();
        stateData.setUserId(userId);
        stateDataDao.save(stateData);
        message.setReplyMarkup(buildMainInlineKeyboardMarkup());
        return message;
    }

}
