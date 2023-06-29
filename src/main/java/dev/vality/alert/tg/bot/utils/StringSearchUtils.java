package dev.vality.alert.tg.bot.utils;

import dev.vality.alert.tg.bot.constants.InlineCommands;
import dev.vality.alerting.mayday.UserAlert;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class StringSearchUtils {

    public static String getSelectParamsInlineQuery(String alertId, String paramId) {
        return String.format("%s[%s]{%s}", InlineCommands.SELECT_PARAM.getCommand(), alertId, paramId);
    }

    public static String getAnswerValueParamsInlineQuery(String alertId, String paramName, String optionValue) {
        return String.format("%s[%s]{%s}<%s>",
                InlineCommands.SELECT_PARAM.getCommand(),
                alertId,
                paramName,
                optionValue);
    }

    public static String substringAlertId(String str) {
        return StringUtils.substringBetween(str, "[", "]");
    }

    public static String substringParamId(String str) {
        return StringUtils.substringBetween(str, "{", "}");
    }

    public static String substringParamValue(String str) {
        return StringUtils.substringBetween(str, "<", ">");
    }

    public static boolean isAlertInList(UserAlert userAlert, String inlineQueryString) {
        return StringUtils.containsIgnoreCase(userAlert.getName(),
                inlineQueryString.substring(InlineCommands.SELECT_ALERT.getCommand().length()).trim())
                || StringUtils.containsIgnoreCase(userAlert.getId(),
                inlineQueryString.substring(InlineCommands.SELECT_ALERT.getCommand().length()).trim());
    }

    public static boolean isParamInList(String option, String inlineQueryString, String alertId, String paramId) {
        return StringUtils.containsIgnoreCase(
                option,
                inlineQueryString.substring(getSelectParamsInlineQuery(alertId, paramId).length()).trim());
    }
}
