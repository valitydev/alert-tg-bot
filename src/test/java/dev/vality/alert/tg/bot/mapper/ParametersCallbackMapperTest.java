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

import static dev.vality.alert.tg.bot.TestObjectFactory.testStateData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(ExcludeDataSourceConfiguration.class)
@ContextConfiguration(classes = {ParametersCallbackMapper.class, JsonMapper.class})
@SpringBootTest(properties = {"spring.config.location=classpath:/application.yml"})
public class ParametersCallbackMapperTest {

    @MockBean
    private StateDataDao stateDataDao;
    @MockBean
    private ParametersDao parametersDao;
    @MockBean
    private MayDayService mayDayService;
    @Autowired
    private ParametersCallbackMapper parametersCallbackMapper;

    @Test
    void testMapParametersCallback() throws Exception {
        List<ParameterConfiguration> parameterConfigurations =
                Collections.singletonList(new ParameterConfiguration()
                        .setId("2").setName("test").setOptions(List.of("test")));
        when(mayDayService.getAlertConfiguration(any()))
                .thenReturn(new AlertConfiguration().setId("test").setParameters(parameterConfigurations));
        when(stateDataDao.getByUserId(any())).thenReturn(testStateData());
        SendMessage sendMessage = parametersCallbackMapper.mapParametersCallback("test", 123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals("Выберете из списка параметр: test", sendMessage.getText());
        verify(mayDayService, times(1)).getAlertConfiguration(any());
        verify(stateDataDao, times(1)).getByUserId(any());
        verify(stateDataDao, times(1)).save(any());
        verify(parametersDao, times(1)).save(any());
    }
}
