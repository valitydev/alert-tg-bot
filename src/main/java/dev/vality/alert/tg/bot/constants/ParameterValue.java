package dev.vality.alert.tg.bot.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParameterValue {

    EMPTY("-", "Перейти к следующему шагу");

    private final String id;
    private final String text;
}
