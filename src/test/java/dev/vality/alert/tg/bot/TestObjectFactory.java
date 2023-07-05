package dev.vality.alert.tg.bot;

import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alerting.mayday.UserAlert;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

import java.util.List;


public abstract class TestObjectFactory {

    public static StateData testStateData() {
        StateData stateData = new StateData();
        stateData.setUserId(122233L);
        stateData.setAlertId("45");
        stateData.setMapParams("[{\"values\":[34],\"id\":\"1\",\"name\":\"Процент\"}]");
        return stateData;
    }

    public static ParametersData testParameters() {
        ParametersData params = new ParametersData();
        params.setAlertId("32");
        params.setParamName("Процент");
        params.setParamId("14");
        params.setOptionsValues("[\"test1\",\"test2\"]");
        params.setMandatory(false);
        params.setMultipleValues(false);
        return params;
    }

    public static List<UserAlert> testUserAlerts() {
        UserAlert userAlert = new UserAlert();
        userAlert.setId("testId");
        userAlert.setName("testName");
        UserAlert userAlert2 = new UserAlert();
        userAlert2.setId("testId2");
        userAlert2.setName("testName2");
        return List.of(userAlert, userAlert2);
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
        message.setText("Введите в ответе параметр: Процент");
        message.setReplyToMessage(message);
        message.setChat(chat);
        message.setFrom(user);
        Update update = new Update();
        update.setMessage(message);
        return update;
    }

    public static Update testUpdateViaBotSelectAlert() {
        User user = new User();
        user.setId(123L);
        Chat chat = new Chat();
        chat.setId(123L);
        Message message = new Message();
        message.setText("selectAlert test");
        message.setReplyToMessage(message);
        message.setChat(chat);
        message.setFrom(user);
        Update update = new Update();
        update.setMessage(message);
        return update;
    }

    public static Update testUpdateViaBotSelectParam() {
        User user = new User();
        user.setId(123L);
        Chat chat = new Chat();
        chat.setId(123L);
        Message message = new Message();
        message.setText("selectParam{Процент}<14>");
        message.setReplyToMessage(message);
        message.setChat(chat);
        message.setFrom(user);
        Update update = new Update();
        update.setMessage(message);
        return update;
    }

    public static Update testUpdateInlineQuerySelectAlert() {
        User user = new User();
        user.setId(123L);
        InlineQuery inlineQuery = new InlineQuery();
        inlineQuery.setId("123");
        inlineQuery.setFrom(user);
        inlineQuery.setQuery("selectAlert");
        Update update = new Update();
        update.setInlineQuery(inlineQuery);
        return update;
    }

    public static Update testUpdateInlineQuerySelectParam() {
        User user = new User();
        user.setId(123L);
        InlineQuery inlineQuery = new InlineQuery();
        inlineQuery.setId("123");
        inlineQuery.setFrom(user);
        inlineQuery.setQuery("selectParam[test1]{test2}");
        Update update = new Update();
        update.setInlineQuery(inlineQuery);
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
        callbackQuery.setData("getAllAlerts");
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public static Update testUpdateReturnCallback() {
        User user = new User();
        user.setId(123L);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(user);
        callbackQuery.setData("return");
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public static Update testUpdateMessageCallback() {
        User user = new User();
        user.setId(123L);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(user);
        callbackQuery.setData("test");
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        return update;
    }

}
