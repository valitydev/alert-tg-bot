package dev.vality.alert.tg.bot.utils;

import dev.vality.alert.tg.bot.constants.MainMenu;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class MainMenuBuilder {

    public static InlineKeyboardMarkup buildMainInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowsInline.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text(MainMenu.CREATE_ALERT.getText())
                .callbackData(MainMenu.CREATE_ALERT.getCallbackData())
                .build()));
        rowsInline.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text(MainMenu.GET_ALL_ALERTS.getText())
                .callbackData(MainMenu.GET_ALL_ALERTS.getCallbackData())
                .build()));
        rowsInline.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text(MainMenu.DELETE_ALERT.getText())
                .callbackData(MainMenu.DELETE_ALERT.getCallbackData())
                .build()));
        rowsInline.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text(MainMenu.DELETE_ALL_ALERTS.getText())
                .callbackData(MainMenu.DELETE_ALL_ALERTS.getCallbackData())
                .build()));
        rowsInline.add(rowInline);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
