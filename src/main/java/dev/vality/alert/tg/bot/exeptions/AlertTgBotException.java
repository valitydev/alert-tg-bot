package dev.vality.alert.tg.bot.exeptions;

public class AlertTgBotException extends RuntimeException {

    public AlertTgBotException(String message) {
        super(message);
    }

    public AlertTgBotException(String message, Throwable cause) {
        super(message, cause);
    }
}
