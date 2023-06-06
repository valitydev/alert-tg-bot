package dev.vality.alert.tg.bot.mapper;

import dev.vality.alert.tg.bot.config.ExcludeDataSourceConfiguration;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.service.MayDayService;
import dev.vality.alerting.mayday.UserAlert;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;
import java.util.List;

import static dev.vality.alert.tg.bot.constants.TextConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(ExcludeDataSourceConfiguration.class)
@ContextConfiguration(classes = {MenuCallbackMapper.class})
public class MenuCallbackMapperTest {

    @MockBean
    private StateDataDao stateDataDao;
    @MockBean
    private MayDayService mayDayService;
    @Autowired
    private MenuCallbackMapper menuCallbackMapper;

    @Test
    void testGetAllAlertsCallback() throws Exception {
        List<UserAlert> userAlertsList = Collections.singletonList(new UserAlert().setId("2").setName("test"));
        when(mayDayService.getUserAlerts(any()))
                .thenReturn(userAlertsList);
        SendMessage sendMessage = menuCallbackMapper.getAllAlertsCallback(123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals(userAlertsList.toString(), sendMessage.getText());
    }

    @Test
    void testDeleteAlertCallback() throws Exception {
        SendMessage sendMessage = menuCallbackMapper.deleteAlertCallback(123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals(ENTER_ALERT_ID_FOR_REMOVED.getText(), sendMessage.getText());
    }

    @Test
    void testDeleteAllAlertsCallback() throws Exception {
        SendMessage sendMessage = menuCallbackMapper.deleteAllAlertsCallback(123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals(ALERTS_REMOVED.getText(), sendMessage.getText());
    }

    @Test
    void testReturnCallback() {
        SendMessage sendMessage = menuCallbackMapper.returnCallback();
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals(SELECT_ACTION.getText(), sendMessage.getText());
    }

    @Test
    void testCreateAlertCallback() throws TException {
        SendMessage sendMessage = menuCallbackMapper.createAlertCallback(123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals(SELECT_ALERT.getText(), sendMessage.getText());
        verify(stateDataDao, times(1)).save(any());
        verify(mayDayService, times(1)).getSupportedAlerts();
    }


}
