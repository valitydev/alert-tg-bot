package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.config.ExcludeDataSourceConfiguration;
import dev.vality.alert.tg.bot.dao.StateDataDao;
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

import static dev.vality.alert.tg.bot.TestObjectFactory.testUpdateMessage;
import static dev.vality.alert.tg.bot.constants.TextConstants.SELECT_ACTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import(ExcludeDataSourceConfiguration.class)
@ContextConfiguration(classes = {MessageHandler.class})
@SpringBootTest(properties = {"spring.config.location=classpath:/application.yml"})
public class MessageHandlerTest {

    @MockBean
    private StateDataDao stateDataDao;
    @Autowired
    private MessageHandler messageHandler;


    @Test
    void testMessageHandle() {
        Update update = testUpdateMessage();
        SendMessage sendMessage = messageHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertNotNull(sendMessage.getReplyMarkup());
        assertEquals(SELECT_ACTION.getText(), sendMessage.getText());
        verify(stateDataDao, times(1)).save(any());
    }

}
