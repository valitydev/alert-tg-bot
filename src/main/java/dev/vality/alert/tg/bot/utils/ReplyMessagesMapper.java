package dev.vality.alert.tg.bot.utils;

import dev.vality.alert.tg.bot.constants.TextConstants;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alert.tg.bot.service.MayDayService;
import dev.vality.alerting.mayday.CreateAlertRequest;
import dev.vality.alerting.mayday.ParameterInfo;
import dev.vality.alerting.mayday.ParameterValue;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.vality.alert.tg.bot.utils.MainMenuBuilder.buildMainInlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class ReplyMessagesMapper {

    private final MayDayService mayDayService;
    private final ParametersDao parametersDao;
    private final StateDataDao stateDataDao;
    private final JsonMapper jsonMapper;

    public SendMessage createAlertRequest(long userId) throws TException {
        StateData stateData = stateDataDao.getByUserId(userId);
        Map<String, String> paramMap = jsonMapper.toMap(stateData.getMapParams());
        CreateAlertRequest createAlertRequest = new CreateAlertRequest();
        createAlertRequest.setAlertId(stateData.getAlertId());
        createAlertRequest.setUserId(String.valueOf(userId));
        List<ParameterInfo> parameterInfos = new ArrayList<>();
        for (String key : paramMap.keySet()) {
            ParametersData parametersData = parametersDao.getByAlertIdAndParamName(stateData.getAlertId(), key);
            ParameterInfo parameterInfo = new ParameterInfo();
            parameterInfo.setParameterId(parametersData.getParamId());
            parameterInfo.setType(mapParameterValue(parametersData, paramMap, key));
            parameterInfos.add(parameterInfo);
        }
        createAlertRequest.setParameters(parameterInfos);
        mayDayService.createAlert(createAlertRequest);
        SendMessage message = new SendMessage();
        message.setText(TextConstants.ALERT_CREATED.getText());
        message.setReplyMarkup(buildMainInlineKeyboardMarkup());
        return message;
    }

    public ParameterValue mapParameterValue(ParametersData parametersData, Map<String, String> paramMap, String key) {
        return switch (parametersData.getParamType()) {
            case str -> ParameterValue.str(paramMap.get(key));
            case integer -> ParameterValue.integer(Long.parseLong(paramMap.get(key)));
            case fl -> ParameterValue.fl(Double.parseDouble(paramMap.get(key)));
            case bl -> ParameterValue.bl(Boolean.parseBoolean(paramMap.get(key)));
        };
    }

    public SendMessage createNextParameterRequest(String text) {
        SendMessage message = new SendMessage();
        message.setReplyMarkup(new ForceReplyKeyboard());
        message.setText(text);
        return message;
    }

    public SendMessage createDeleteAlert(String text) throws TException {
        SendMessage message = new SendMessage();
        mayDayService.deleteAlert(text);
        message.setText(TextConstants.ALERT_REMOVED.getText());
        message.setReplyMarkup(buildMainInlineKeyboardMarkup());
        return message;
    }
}
