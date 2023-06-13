package dev.vality.alert.tg.bot.handler;

import org.apache.thrift.TException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommonHandler {

    boolean filter(final Update update);

    SendMessage handle(Update update, long userId) throws TException;

}
