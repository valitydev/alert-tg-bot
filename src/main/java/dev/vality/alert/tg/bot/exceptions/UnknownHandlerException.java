package dev.vality.alert.tg.bot.exceptions;

public class UnknownHandlerException extends RuntimeException {

    public UnknownHandlerException() {
        super("Unknown handler");
    }
}
