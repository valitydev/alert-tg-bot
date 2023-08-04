package dev.vality.alert.tg.bot.service;


import dev.vality.alerting.tg_bot.Notification;
import dev.vality.alerting.tg_bot.NotifierServiceSrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifierService implements NotifierServiceSrv.Iface {

    private final AlertBot alertBot;

    @Override
    public void notify(Notification notification) {
        log.info("Try to send notification {} to user {}", notification.getMessage(), notification.getReceiverId());
        try {
            if (alertBot.isChatMemberPermission(Long.valueOf(notification.getReceiverId()))) {
                alertBot.execute(SendMessage.builder()
                        .chatId(Long.valueOf(notification.getReceiverId()))
                        .text(notification.getMessage()).build());

                log.info("Notification {} to user {} was send",
                        notification.getMessage(),
                        notification.getReceiverId());
            }
        } catch (TelegramApiException | TException ex) {
            log.error("Received an exception while notify receiver {} with message {}",
                    notification.getReceiverId(),
                    notification.getMessage(),
                    ex);
        }
    }

}
