package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.mapper.MenuCallbackMapper;
import dev.vality.alert.tg.bot.mapper.ParametersCallbackMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
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
        switch (callbackData) {
            case "createAlert" -> {
                return menuCallbackMapper.createAlertCallback(userId);
            }
            case "getAllAlerts" -> {
                return menuCallbackMapper.getAllAlertsCallback(userId);
            }
            case "deleteAlert" -> {
                return menuCallbackMapper.deleteAlertCallback(userId);
            }
            case "deleteAllAlerts" -> {
                return menuCallbackMapper.deleteAllAlertsCallback(userId);
            }
            case "return" -> {
                return menuCallbackMapper.returnCallback();
            }
            default -> {
                return parametersCallbackMapper.mapParametersCallback(callbackData, userId);
            }
        }
    }

}
