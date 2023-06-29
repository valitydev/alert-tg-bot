package dev.vality.alert.tg.bot.mapper;

import dev.vality.alert.tg.bot.config.ExcludeDataSourceConfiguration;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.service.MayDayService;
import dev.vality.alerting.mayday.AlertConfiguration;
import dev.vality.alerting.mayday.ParameterConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;
import java.util.List;

import static dev.vality.alert.tg.bot.TestObjectFactory.testParameters;
import static dev.vality.alert.tg.bot.TestObjectFactory.testStateData;
import static dev.vality.alert.tg.bot.constants.TextConstants.ALERT_CREATED;
import static dev.vality.alert.tg.bot.constants.TextConstants.ALERT_REMOVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(ExcludeDataSourceConfiguration.class)
@ContextConfiguration(classes = {ReplyMessagesMapper.class, JsonMapper.class})
@SpringBootTest(properties = {"spring.config.location=classpath:/application.yml"})
public class ReplyMessageMapperTest {

    @MockBean
    private StateDataDao stateDataDao;
    @MockBean
    private ParametersDao parametersDao;
    @MockBean
    private MayDayService mayDayService;
    @Autowired
    private ReplyMessagesMapper replyMessagesMapper;

    @Test
    void testCreateAlertRequest() throws Exception {
        List<ParameterConfiguration> parameterConfigurations =
                Collections.singletonList(new ParameterConfiguration()
                        .setId("2").setName("test").setOptions(List.of("test")));
        when(mayDayService.getAlertConfiguration(any()))
                .thenReturn(new AlertConfiguration().setId("test").setParameters(parameterConfigurations));
        when(stateDataDao.getByUserId(any())).thenReturn(testStateData());
        when(parametersDao.getByAlertIdAndParamName(any(), any())).thenReturn(testParameters());
        SendMessage sendMessage = replyMessagesMapper.createAlertRequest(123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals(ALERT_CREATED.getText(), sendMessage.getText());
        verify(mayDayService, times(1)).createAlert(any());
        verify(stateDataDao, times(1)).getByUserId(any());
    }

    @Test
    void testCreateNextParameterRequest() {
        when(parametersDao.getByAlertIdAndParamName(any(), any())).thenReturn(testParameters());
        SendMessage sendMessage = replyMessagesMapper.createNextParameterRequest("test", testStateData());
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals("Выберите из списка параметр: Терминал", sendMessage.getText());
    }

    @Test
    void testDeleteAlert() throws Exception {
        SendMessage sendMessage = replyMessagesMapper.deleteAlert(123L, "test");
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals(ALERT_REMOVED.getText(), sendMessage.getText());
        verify(mayDayService, times(1)).deleteAlert(any(), any());
    }
}
