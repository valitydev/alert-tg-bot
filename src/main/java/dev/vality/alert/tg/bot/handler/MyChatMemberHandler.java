package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.service.MayDayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyChatMemberHandler implements CommonHandler<SendMessage> {

    private final MayDayService mayDayService;

    @Override
    public boolean filter(Update update) {
        return update.hasMyChatMember();
    }

    @Override
    public SendMessage handle(Update update, long userId) throws TException {
        if (update.getMyChatMember().getOldChatMember().getStatus().equals("member")
                && update.getMyChatMember().getNewChatMember().getStatus().equals("kicked")) {
            mayDayService.deleteAllAlerts(String.valueOf(userId));
            log.info("User {} blocked bot, all alerts was deleted for user", userId);
        }
        return null;
    }

}
