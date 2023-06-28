package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.config.ExcludeDataSourceConfiguration;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.mapper.CreateParamsRequestMapper;
import dev.vality.alert.tg.bot.mapper.JsonMapper;
import dev.vality.alert.tg.bot.mapper.ReplyMessagesMapper;
import dev.vality.alert.tg.bot.service.MayDayService;
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

import static dev.vality.alert.tg.bot.TestObjectFactory.*;
import static dev.vality.alert.tg.bot.constants.TextConstants.ALERT_CREATED;
import static dev.vality.alert.tg.bot.constants.TextConstants.ALERT_REMOVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(ExcludeDataSourceConfiguration.class)
@ContextConfiguration(classes = {ViaBotReplyHandler.class,
        ReplyMessagesMapper.class, JsonMapper.class, CreateParamsRequestMapper.class})
@SpringBootTest(properties = {"spring.config.location=classpath:/application.yml"})
public class ViaBotReplyHandlerTest {

    @MockBean
    private ParametersDao parametersDao;
    @MockBean
    private StateDataDao stateDataDao;
    @MockBean
    private MayDayService mayDayService;
    @Autowired
    private CreateParamsRequestMapper createParamsRequestMapper;
    @Autowired
    private ViaBotReplyHandler replyHandler;


    @Test
    void testSelectAlert() throws Exception {
        Update update = testUpdateViaBotSelectAlert();
        SendMessage sendMessage = replyHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertEquals(ALERT_REMOVED.getText(), sendMessage.getText());
        verify(mayDayService, times(1)).deleteAlert(any(), any());
    }

    @Test
    void testSelectParam() throws Exception {
        when(stateDataDao.getByUserId(anyLong())).thenReturn(testStateData());
        when(parametersDao.getByAlertIdAndParamName(anyString(), anyString())).thenReturn(testParameters());
        Update update = testUpdateViaBotSelectParam();
        SendMessage sendMessage = replyHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertEquals(ALERT_CREATED.getText(), sendMessage.getText());
        verify(mayDayService, times(1)).createAlert(any());
        verify(stateDataDao, times(2)).getByUserId(any());
        verify(stateDataDao, times(1)).updateParams(any(), any());
    }
}
