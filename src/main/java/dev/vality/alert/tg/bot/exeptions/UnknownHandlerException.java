package dev.vality.alert.tg.bot.exeptions;

public class UnknownHandlerException extends RuntimeException {

    public UnknownHandlerException() {
        super("Unknown handler");
    }
}
