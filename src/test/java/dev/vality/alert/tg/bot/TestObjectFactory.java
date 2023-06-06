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
        Chat chat = new Chat();
        chat.setId(123L);
        User user = new User();
        user.setId(123L);
        Message message = new Message();
        message.setText("test");
        message.setChat(chat);
        message.setFrom(user);
        Update update = new Update();
        update.setMessage(message);
        return update;
    }

    public static Update testUpdateMessageWithWithDifferentId() {
        Chat chat = new Chat();
        chat.setId(123L);
        User user = new User();
        user.setId(567L);
        Message message = new Message();
        message.setText("test");
        message.setChat(chat);
        message.setFrom(user);
        Update update = new Update();
        update.setMessage(message);
        return update;
    }

    public static Update testUpdateReply() {
        User user = new User();
        user.setId(123L);
        Chat chat = new Chat();
        chat.setId(123L);
        Message message = new Message();
        message.setText("test");
        message.setReplyToMessage(message);
        message.setChat(chat);
        message.setFrom(user);
        Update update = new Update();
        update.setMessage(message);
        return update;
    }

    public static Update testUpdateDeleteAllCallback() {
        User user = new User();
        user.setId(123L);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(user);
        callbackQuery.setData("deleteAllAlerts");
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public static Update testUpdateCreateAlertCallback() {
        User user = new User();
        user.setId(123L);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(user);
        callbackQuery.setData("createAlert");
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public static Update testUpdateGetAllAlertsCallback() {
        User user = new User();
        user.setId(123L);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(user);
        callbackQuery.setData("return");
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        return update;
    }

}
