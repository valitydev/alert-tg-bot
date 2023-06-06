package dev.vality.alert.tg.bot.handler;

import dev.vality.alert.tg.bot.constants.TextConstants;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alert.tg.bot.mapper.JsonMapper;
import dev.vality.alert.tg.bot.mapper.ReplyMessagesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplyHandler implements CommonHandler {

    private final StateDataDao stateDataDao;
    private final ReplyMessagesMapper replyMessagesMapper;
    private final JsonMapper jsonMapper;

    @Override
    public boolean filter(Update update) {
        return update.hasMessage()
                && update.getMessage().getReplyToMessage() != null
                && Objects.equals(update.getMessage().getChatId(), update.getMessage().getFrom().getId());
    }

    @Override
    public SendMessage handle(Update update, long userId) throws TException {
        if (isReplyMessageDeleteAlert(update)) {
            return replyMessagesMapper.deleteAlert(update.getMessage().getText());
        } else {
            StateData stateData = stateDataDao.getByUserId(userId);
            Map<String, String> paramMap = jsonMapper.toMap(stateData.getMapParams());
            String replyText = update.getMessage().getReplyToMessage().getText();
            String answerText = update.getMessage().getText();
            paramMap.put(replyText, answerText);
            stateData.setMapParams(jsonMapper.toJson(paramMap));
            stateDataDao.updateParams(userId, stateData.getMapParams());
            String nextKey = getNextKeyForFill(paramMap);
            if (nextKey != null) {
                return replyMessagesMapper.createNextParameterRequest(nextKey);
            } else {
                return replyMessagesMapper.createAlertRequest(userId);
            }
        }
    }

    private boolean isReplyMessageDeleteAlert(Update update) {
        return update.getMessage().getReplyToMessage().getText()
                .equals(TextConstants.ENTER_ALERT_ID_FOR_REMOVED.getText());
    }

    private static String getNextKeyForFill(Map<String, String> map) {
        return map.entrySet().stream()
                .filter(entry -> null == entry.getValue())
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }
}

