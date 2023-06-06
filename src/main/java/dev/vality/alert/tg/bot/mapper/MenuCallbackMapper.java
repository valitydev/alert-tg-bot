package dev.vality.alert.tg.bot.mapper;

import dev.vality.alert.tg.bot.constants.MainMenu;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alert.tg.bot.service.MayDayService;
import dev.vality.alerting.mayday.Alert;
import dev.vality.alerting.mayday.UserAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.vality.alert.tg.bot.constants.TextConstants.*;
import static dev.vality.alert.tg.bot.utils.MainMenuBuilder.buildMainInlineKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuCallbackMapper {

    private final MayDayService mayDayService;
    private final StateDataDao stateDataDao;

    public SendMessage createAlertCallback(long userId) throws TException {
        StateData stateData = new StateData();
        stateData.setUserId(userId);
        stateDataDao.save(stateData);
        SendMessage message = new SendMessage();
        message.setText(SELECT_ALERT.getText());
        List<Alert> alerts = mayDayService.getSupportedAlerts();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        alerts.forEach(alert -> rowsInline.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text(alert.getName())
                .callbackData(alert.getAlert())
                .build())));
        rowsInline.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text(MainMenu.RETURN_TO_MENU.getText())
                .callbackData(MainMenu.RETURN_TO_MENU.getCallbackData())
                .build()));
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowsInline.add(rowInline);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }

    public SendMessage getAllAlertsCallback(long userId) throws TException {
        SendMessage message = new SendMessage();
        List<UserAlert> userAlerts = mayDayService.getUserAlerts(String.valueOf(userId));
        message.setText(String.valueOf(userAlerts));
        message.setReplyMarkup(buildMainInlineKeyboardMarkup());
        return message;
    }

    public SendMessage deleteAlertCallback(long userId) throws TException {
        SendMessage message = new SendMessage();
        mayDayService.deleteAlert(String.valueOf(userId));
        message.setText(ENTER_ALERT_ID_FOR_REMOVED.getText());
        message.setReplyMarkup(new ForceReplyKeyboard());
        return message;
    }

    public SendMessage deleteAllAlertsCallback(long userId) throws TException {
        log.info("User {} delete all alerts", userId);
        SendMessage message = new SendMessage();
        mayDayService.deleteAllAlerts(String.valueOf(userId));
        message.setText(ALERTS_REMOVED.getText());
        message.setReplyMarkup(buildMainInlineKeyboardMarkup());
        return message;
    }

    public SendMessage returnCallback() {
        SendMessage message = new SendMessage();
        message.setText(SELECT_ACTION.getText());
        message.setReplyMarkup(buildMainInlineKeyboardMarkup());
        return message;
    }
}
