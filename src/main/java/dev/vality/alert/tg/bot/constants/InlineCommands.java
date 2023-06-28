package dev.vality.alert.tg.bot.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum InlineCommands {

    SELECT_ALERT("selectAlert"),
    SELECT_PARAM("selectParam");

    private final String command;

    public static InlineCommands valueOfStartInlineCommand(String value) {
        return Arrays.stream(InlineCommands.values())
                .filter(inlineCommand -> value.startsWith(inlineCommand.getCommand()))
                .findFirst().orElse(SELECT_PARAM);
    }

}
