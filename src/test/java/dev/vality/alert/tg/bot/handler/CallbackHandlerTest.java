package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.config.ExcludeDataSourceConfiguration;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.mapper.JsonMapper;
import dev.vality.alert.tg.bot.mapper.MenuCallbackMapper;
import dev.vality.alert.tg.bot.mapper.ParametersCallbackMapper;
import dev.vality.alert.tg.bot.service.MayDayService;
import dev.vality.alerting.mayday.AlertConfiguration;
import dev.vality.alerting.mayday.ParameterConfiguration;
import dev.vality.alerting.mayday.ParameterType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

import static dev.vality.alert.tg.bot.TestObjectFactory.*;
import static dev.vality.alert.tg.bot.constants.TextConstants.ALERTS_REMOVED;
import static dev.vality.alert.tg.bot.constants.TextConstants.SELECT_ALERT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(ExcludeDataSourceConfiguration.class)
@ContextConfiguration(classes = {CallbackHandler.class, MenuCallbackMapper.class, ParametersCallbackMapper.class,
        JsonMapper.class})
@SpringBootTest(properties = {"spring.config.location=classpath:/application.yml"})
public class CallbackHandlerTest {
    @MockBean
    private ParametersDao parametersDao;
    @MockBean
    private StateDataDao stateDataDao;
    @MockBean
    private MayDayService mayDayService;
    @Autowired
    private CallbackHandler callbackHandler;


    @Test
    void testCallbackDeleteAllAlertsHandler() throws Exception {
        Update update = testUpdateDeleteAllCallback();
        SendMessage sendMessage = callbackHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertEquals(ALERTS_REMOVED.getText(), sendMessage.getText());
        verify(mayDayService, times(1)).deleteAllAlerts(any());
    }

    @Test
    void testCallbackCreateAlertHandler() throws Exception {
        Update update = testUpdateCreateAlertCallback();
        SendMessage sendMessage = callbackHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertEquals(SELECT_ALERT.getText(), sendMessage.getText());
        verify(stateDataDao, times(1)).save(any());

    }

    @Test
    void testCallbackGetAllAlertsHandler() throws Exception {
        Update update = testUpdateGetAllAlertsCallback();
        SendMessage sendMessage = callbackHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        verify(mayDayService, times(1)).getUserAlerts(any());

    }

    @Test
    void testCallbackReturnHandler() throws Exception {
        Update update = testUpdateReturnCallback();
        SendMessage sendMessage = callbackHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
    }

    @Test
    void testCallbackMessageHandler() throws Exception {
        List<ParameterConfiguration> parameterConfigurations =
                Collections.singletonList(new ParameterConfiguration()
                        .setId("2").setName("test").setType(ParameterType.str));
        when(mayDayService.getAlertConfiguration(any()))
                .thenReturn(new AlertConfiguration().setAlertId("test").setParameters(parameterConfigurations));
        when(stateDataDao.getByUserId(any())).thenReturn(testStateData());
        Update update = testUpdateMessageCallback();
        SendMessage sendMessage = callbackHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        verify(mayDayService, times(1)).getAlertConfiguration(any());
        verify(stateDataDao, times(1)).getByUserId(any());
        verify(stateDataDao, times(1)).save(any());
        verify(parametersDao, times(1)).save(any());

    }

}
