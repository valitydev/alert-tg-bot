package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.constants.MainMenu;
import dev.vality.alert.tg.bot.mapper.MenuCallbackMapper;
import dev.vality.alert.tg.bot.mapper.ParametersCallbackMapper;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CallbackHandler implements CommonHandler {

    private final MenuCallbackMapper menuCallbackMapper;
    private final ParametersCallbackMapper parametersCallbackMapper;

    @Override
    public boolean filter(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public SendMessage handle(Update update, long userId) throws TException {
        String callbackData = update.getCallbackQuery().getData();
        return switch (MainMenu.valueOfCallbackData(callbackData)) {
            case CREATE_ALERT -> menuCallbackMapper.createAlertCallback(userId);
            case GET_ALL_ALERTS -> menuCallbackMapper.getAllAlertsCallback(userId);
            case DELETE_ALERT -> menuCallbackMapper.deleteAlertCallback();
            case DELETE_ALL_ALERTS -> menuCallbackMapper.deleteAllAlertsCallback(userId);
            case RETURN_TO_MENU -> menuCallbackMapper.returnCallback();
            case CONFIGURE_PARAM -> parametersCallbackMapper.mapParametersCallback(callbackData, userId);
        };
    }

}
