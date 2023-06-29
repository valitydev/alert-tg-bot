package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.config.ExcludeDataSourceConfiguration;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.mapper.JsonMapper;
import dev.vality.alert.tg.bot.service.MayDayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.vality.alert.tg.bot.TestObjectFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(ExcludeDataSourceConfiguration.class)
@ContextConfiguration(classes = {InlineHandler.class, JsonMapper.class})
@SpringBootTest(properties = {"spring.config.location=classpath:/application.yml"})
public class InlineHandlerTest {

    @MockBean
    private ParametersDao parametersDao;
    @MockBean
    private MayDayService mayDayService;
    @Autowired
    private InlineHandler inlineHandler;


    @Test
    void testSelectAlert() throws Exception {
        when(mayDayService.getUserAlerts(anyString())).thenReturn(testUserAlerts());
        Update update = testUpdateInlineQuerySelectAlert();
        AnswerInlineQuery answerInlineQuery = inlineHandler.handle(update, 123L);
        assertNotNull(answerInlineQuery);
        assertEquals(2, answerInlineQuery.getResults().size());
        verify(mayDayService, times(1)).getUserAlerts(any());
    }

    @Test
    void testSelectParam() throws Exception {
        when(parametersDao.getByAlertIdAndParamId(any(), any())).thenReturn(testParameters());
        Update update = testUpdateInlineQuerySelectParam();
        AnswerInlineQuery answerInlineQuery = inlineHandler.handle(update, 123L);
        assertNotNull(answerInlineQuery);
        assertEquals(2, answerInlineQuery.getResults().size());
        verify(parametersDao, times(1)).getByAlertIdAndParamId(any(), any());

    }
}
