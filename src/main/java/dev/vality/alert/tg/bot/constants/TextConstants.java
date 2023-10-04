package dev.vality.alert.tg.bot.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TextConstants {

    SELECT_ACTION("Выберите действие"),
    REPLY_OR_ACTION("Ответьте на предыдущее сообщение или выберите действие"),
    ENTER_PARAMETER("Введите параметр %s в формате %s"),
    ALERT_CREATED("Алерт создан"),
    ENTER_ALERT_ID_FOR_REMOVED("Введите id алерта для удаления"),
    DELETE_ALERT("Удалить алерт"),
    SELECT_ALERT("Выберите алерт"),
    ALERT_REMOVED("Алерт удален"),
    ALERTS_REMOVED("Алерты удалены"),
    NO_ALERTS_FOUND("У вас нет созданных алертов"),
    DELETE_ALL_ALERTS("Удалить все алерты"),
    SELECT_PARAM_FROM_LIST("Выберите из списка параметр: "),
    ENTER_PARAM_TO_REPLY("Введите в ответе параметр: "),
    SELECT("Выбрать");

    private final String text;

}
