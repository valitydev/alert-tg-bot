package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.constants.TextConstants;
import dev.vality.alert.tg.bot.mapper.CreateParamsRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.vality.alert.tg.bot.utils.UserUtils.isUserInBot;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ForceReplyHandler implements CommonHandler<SendMessage> {

    private final CreateParamsRequestMapper createParamsRequestMapper;

    @Override
    public boolean filter(Update update) {
        return update.hasMessage()
                && update.getMessage().getReplyToMessage() != null
                && isUserInBot(update);
    }

    @Override
    public SendMessage handle(Update update, long userId) throws TException {
        return createParamsRequestMapper.createRequest(
                userId,
                update.getMessage().getReplyToMessage().getText(),
                update.getMessage().getText());
    }
}

