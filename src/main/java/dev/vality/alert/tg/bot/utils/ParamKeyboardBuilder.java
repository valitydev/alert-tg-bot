package dev.vality.alert.tg.bot.utils;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.vality.alert.tg.bot.constants.TextConstants.SELECT;
import static dev.vality.alert.tg.bot.constants.TextConstants.SELECT_PARAM_FROM_LIST;
import static dev.vality.alert.tg.bot.utils.StringSearchUtils.getSelectParamsInlineQuery;

@RequiredArgsConstructor
public class ParamKeyboardBuilder {

    public static SendMessage buildParamKeyboard(boolean isSetOptions,
                                                 String alertId,
                                                 String paramId,
                                                 String paramNameForSelect,
                                                 String paramNameForReply) {
        SendMessage message = new SendMessage();
        if (isSetOptions) {
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowsInline.add(Collections.singletonList(InlineKeyboardButton.builder()
                    .text(SELECT.getText())
                    .switchInlineQueryCurrentChat(
                            getSelectParamsInlineQuery(alertId, paramId))
                    .build()));
            rowsInline.add(rowInline);
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard(rowsInline);
            message.setReplyMarkup(markupInline);
            message.setText(SELECT_PARAM_FROM_LIST.getText() + paramNameForSelect);
        } else {
            message.setReplyMarkup(new ForceReplyKeyboard());
            message.setText(paramNameForReply);
        }
        return message;
    }

}
