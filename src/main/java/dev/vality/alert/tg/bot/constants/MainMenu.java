package dev.vality.alert.tg.bot.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MainMenu {

    CREATE_ALERT("Создать новый алерт", "createAlert"),
    GET_ALL_ALERTS("Получить все созданные алерты", "getAllAlerts"),
    DELETE_ALERT("Удалить алерт", "deleteAlert"),
    RETURN_TO_MENU("Возврат в меню", "return"),
    DELETE_ALL_ALERTS("Удалить все алерты", "deleteAllAlerts");

    private final String text;

    private final String callbackData;

    public static MainMenu valueOfCallbackData(String callbackData) {
        return Arrays.stream(MainMenu.values()).filter(mainMenu -> mainMenu.getCallbackData().equals(callbackData))
                .findFirst().orElse(null);
    }

}
