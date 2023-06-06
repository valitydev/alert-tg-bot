package dev.vality.alert.tg.bot;

import dev.vality.alert.tg.bot.domain.enums.ParameterType;
import dev.vality.alert.tg.bot.domain.tables.pojos.Parameters;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import org.telegram.telegrambots.meta.api.objects.*;


public abstract class TestObjectFactory {

    public static StateData testStateData() {
        StateData stateData = new StateData();
        stateData.setUserId(122233L);
        stateData.setAlertId("45");
        stateData.setMapParams("{\"Процент\":34,\"Имя Терминала\":56}");
        return stateData;
    }

    public static Parameters testParameters() {
        Parameters params = new Parameters();
        params.setAlertId("32");
        params.setParamName("Терминал");
        params.setParamId("14");
        params.setParamType(ParameterType.str);
        return params;
    }

    public static Update testUpdateMessage() {
        Update update = new Update();
        Message message = new Message();
        message.setText("test");
        Chat chat = new Chat();
        chat.setId(123L);
        message.setChat(chat);
        User user = new User();
        user.setId(123L);
        message.setFrom(user);
        update.setMessage(message);
        return update;
    }

    public static Update testUpdateMessageWithWithDifferentId() {
        Update update = new Update();
        Message message = new Message();
        message.setText("test");
        Chat chat = new Chat();
        chat.setId(123L);
        message.setChat(chat);
        User user = new User();
        user.setId(567L);
        message.setFrom(user);
        update.setMessage(message);
        return update;
    }

    public static Update testUpdateReply() {
        Update update = new Update();
        Message message = new Message();
        message.setText("test");
        message.setReplyToMessage(message);
        Chat chat = new Chat();
        chat.setId(123L);
        message.setChat(chat);
        User user = new User();
        user.setId(123L);
        message.setFrom(user);
        update.setMessage(message);
        return update;
    }

    public static Update testUpdateDeleteAllCallback() {
        Update update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        User user = new User();
        user.setId(123L);
        callbackQuery.setFrom(user);
        callbackQuery.setData("deleteAllAlerts");
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public static Update testUpdateCreateAlertCallback() {
        Update update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        User user = new User();
        user.setId(123L);
        callbackQuery.setFrom(user);
        callbackQuery.setData("createAlert");
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public static Update testUpdateGetAllAlertsCallback() {
        Update update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        User user = new User();
        user.setId(123L);
        callbackQuery.setFrom(user);
        callbackQuery.setData("return");
        update.setCallbackQuery(callbackQuery);
        return update;
    }

}
