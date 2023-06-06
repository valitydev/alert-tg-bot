package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.config.ExcludeDataSourceConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.vality.alert.tg.bot.TestObjectFactory.testUpdateMessage;
import static dev.vality.alert.tg.bot.TestObjectFactory.testUpdateMessageWithWithDifferentId;
import static dev.vality.alert.tg.bot.constants.TextConstants.REPLY_OR_ACTION;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(ExcludeDataSourceConfiguration.class)
@ContextConfiguration(classes = {MainMenuHandler.class})
public class MainMenuHandlerTest {

    @Autowired
    private MainMenuHandler mainMenuHandler;


    @Test
    void testMainMenuWithWithDifferentIdHandle() {
        Update update = testUpdateMessageWithWithDifferentId();
        SendMessage sendMessage = mainMenuHandler.handle(update, 123L);
        assertNull(sendMessage);
    }

    @Test
    void testMainMenuHandle() {
        Update update = testUpdateMessage();
        SendMessage sendMessage = mainMenuHandler.handle(update, 123L);
        assertNotNull(sendMessage);
        assertEquals(REPLY_OR_ACTION.getText(), sendMessage.getText());
    }
}
