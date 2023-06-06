package dev.vality.alert.tg.bot.mapper;

import dev.vality.alert.tg.bot.config.ExcludeDataSourceConfiguration;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.handler.CallbackHandler;
import dev.vality.alert.tg.bot.handler.MainMenuHandler;
import dev.vality.alert.tg.bot.handler.MessageHandler;
import dev.vality.alert.tg.bot.handler.ReplyHandler;
import dev.vality.alert.tg.bot.service.MayDayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.vality.alert.tg.bot.TestObjectFactory.*;
import static dev.vality.alert.tg.bot.constants.TextConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import(ExcludeDataSourceConfiguration.class)
@ContextConfiguration(classes = {MessageHandler.class, MainMenuHandler.class, ReplyHandler.class,
        ReplyMessagesMapper.class, JsonMapper.class, CallbackHandler.class, MenuCallbackMapper.class,
        ParametersCallbackMapper.class})
public class MapperTest {

    @MockBean
    private ParametersDao parametersDao;
    @MockBean
    private StateDataDao stateDataDao;
    @MockBean
    private MayDayService mayDayService;
    @Autowired
    private MessageHandler messageHandler;
    @Autowired
    private MainMenuHandler mainMenuHandler;
    @Autowired
    private ReplyHandler replyHandler;
    @Autowired
    private CallbackHandler callbackHandler;
    @Autowired
    private ReplyMessagesMapper replyMessagesMapper;
    @Autowired
    private JsonMapper jsonMapper;
    @Autowired
    private MenuCallbackMapper menuCallbackMapper;
    @Autowired
    private ParametersCallbackMapper parametersCallbackMapper;

    @Test
    void testMessageHandle() throws Exception {
        Update update = testUpdateMessage();
        SendMessage sendMessage = messageHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals(SELECT_ACTION.getText(), sendMessage.getText());
        verify(stateDataDao, times(1)).save(any());
    }

    @Test
    void testMainMenuHandle() throws Exception {
        Update update = testUpdateMessageWithWithDifferentId();
        SendMessage sendMessage = mainMenuHandler.handle(update, 123L);
        assertNull(sendMessage);
    }

    @Test
    void testReplyHandle() throws Exception {
        mockReplyData();
        Update update = testUpdateReply();
        SendMessage sendMessage = replyHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertEquals(ALERT_CREATED.getText(), sendMessage.getText());
    }

    @Test
    void testCallbackDeleteAllAlertsHandler() throws Exception {
        Update update = testUpdateDeleteAllCallback();
        SendMessage sendMessage = callbackHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertEquals(ALERTS_REMOVED.getText(), sendMessage.getText());
    }

    @Test
    void testCallbackCreateAlertHandler() throws Exception {
        Update update = testUpdateCreateAlertCallback();
        SendMessage sendMessage = callbackHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertEquals(SELECT_ALERT.getText(), sendMessage.getText());
    }

    @Test
    void testCallbackGetAllAlertsHandler() throws Exception {
        Update update = testUpdateGetAllAlertsCallback();
        SendMessage sendMessage = callbackHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
    }

    @Test
    void testCallbackReturnHandler() throws Exception {
        Update update = testUpdateGetAllAlertsCallback();
        SendMessage sendMessage = callbackHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
    }

    protected void mockReplyData() {
        Mockito.when(stateDataDao.getByUserId(anyLong())).thenReturn(testStateData());
        Mockito.when(parametersDao.getByAlertIdAndParamName(anyString(), anyString())).thenReturn(testParameters());
    }

}
