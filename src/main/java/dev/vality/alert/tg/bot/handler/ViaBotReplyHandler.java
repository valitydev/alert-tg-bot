package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.constants.InlineCommands;
import dev.vality.alert.tg.bot.mapper.CreateParamsRequestMapper;
import dev.vality.alert.tg.bot.mapper.ReplyMessagesMapper;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.vality.alert.tg.bot.utils.StringSearchUtils.substringParamId;
import static dev.vality.alert.tg.bot.utils.StringSearchUtils.substringParamValue;
import static dev.vality.alert.tg.bot.utils.UserUtils.isUserInBot;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ViaBotReplyHandler implements CommonHandler<SendMessage> {

    private final ReplyMessagesMapper replyMessagesMapper;
    private final CreateParamsRequestMapper createParamsRequestMapper;

    @Override
    public boolean filter(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getReplyToMessage() == null
                && update.getMessage().hasViaBot()
                && isUserInBot(update);
    }

    @Override
    public SendMessage handle(Update update, long userId) throws TException {
        String text = update.getMessage().getText();
        return switch (InlineCommands.valueOfStartInlineCommand(text)) {
            case SELECT_ALERT -> replyMessagesMapper.deleteAlert(
                    userId,
                    text.substring(InlineCommands.SELECT_ALERT.getCommand().length()).trim());
            case SELECT_PARAM -> createParamsRequestMapper.createRequest(
                    userId,
                    substringParamId(text),
                    substringParamValue(text));
        };
    }

}
