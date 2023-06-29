package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.constants.InlineCommands;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import dev.vality.alert.tg.bot.exeptions.AlertTgBotException;
import dev.vality.alert.tg.bot.mapper.JsonMapper;
import dev.vality.alert.tg.bot.service.MayDayService;
import dev.vality.alerting.mayday.UserAlert;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.thrift.TException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;

import java.util.ArrayList;
import java.util.List;

import static dev.vality.alert.tg.bot.utils.StringSearchUtils.*;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InlineHandler implements CommonHandler<AnswerInlineQuery> {

    private final MayDayService mayDayService;
    private final JsonMapper jsonMapper;
    private final ParametersDao parametersDao;

    @Override
    public boolean filter(Update update) {
        return update.hasInlineQuery();
    }

    @SneakyThrows
    @Override
    public AnswerInlineQuery handle(Update update, long userId) throws TException {
        String inlineQuery = update.getInlineQuery().getQuery();
        List<InlineQueryResult> queryResultArticleList = new ArrayList<>();
        switch (InlineCommands.valueOfStartInlineCommand(inlineQuery)) {
            case SELECT_ALERT -> {
                List<UserAlert> userAlerts = mayDayService.getUserAlerts(
                        String.valueOf(update.getInlineQuery().getFrom().getId()));
                userAlerts.forEach(option -> {
                    if (isAlertInList(option, inlineQuery)) {
                        queryResultArticleList.add(fillInlineQueryResultArticle(
                                option.getId(),
                                option.getName(),
                                new InputTextMessageContent(
                                        InlineCommands.SELECT_ALERT.getCommand() + option.getId())));
                    }
                });

            }
            case SELECT_PARAM -> {
                String alertId = substringAlertId(inlineQuery);
                String paramId = substringParamId(inlineQuery);
                ParametersData parametersData = parametersDao.getByAlertIdAndParamId(alertId, paramId);
                List<String> options = jsonMapper.toList(parametersData.getOptionsValues());
                options.forEach(optionValue -> {
                    if (isParamInList(optionValue, inlineQuery, alertId, paramId)) {
                        queryResultArticleList.add(fillInlineQueryResultArticle(
                                optionValue,
                                null,
                                new InputTextMessageContent(
                                        getAnswerValueParamsInlineQuery(
                                                alertId,
                                                parametersData.getParamName(),
                                                optionValue))));
                    }
                });
            }
            default -> throw new AlertTgBotException("Unknown InlineQuery value: " + inlineQuery);

        }
        return new AnswerInlineQuery(update.getInlineQuery().getId(), queryResultArticleList);
    }

    private InlineQueryResultArticle fillInlineQueryResultArticle(String id,
                                                                  String description,
                                                                  InputTextMessageContent inputTextMessageContent) {
        InlineQueryResultArticle inlineQueryResultArticle = new InlineQueryResultArticle();
        inlineQueryResultArticle.setId(id);
        inlineQueryResultArticle.setTitle(id);
        inlineQueryResultArticle.setDescription(description);
        inlineQueryResultArticle.setInputMessageContent(inputTextMessageContent);
        return inlineQueryResultArticle;
    }

}
