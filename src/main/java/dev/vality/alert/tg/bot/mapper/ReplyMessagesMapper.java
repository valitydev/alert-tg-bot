package dev.vality.alert.tg.bot.mapper;

import dev.vality.alert.tg.bot.constants.TextConstants;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alert.tg.bot.service.MayDayService;
import dev.vality.alert.tg.bot.utils.ParamKeyboardBuilder;
import dev.vality.alerting.mayday.CreateAlertRequest;
import dev.vality.alerting.mayday.ParameterInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Map;

import static dev.vality.alert.tg.bot.utils.MainMenuBuilder.buildMainInlineKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplyMessagesMapper {

    private final MayDayService mayDayService;
    private final ParametersDao parametersDao;
    private final StateDataDao stateDataDao;
    private final JsonMapper jsonMapper;

    public SendMessage createAlertRequest(long userId) throws TException {
        StateData stateData = stateDataDao.getByUserId(userId);
        log.info("Start create alert with stateData {}", stateData);
        Map<String, List<String>> paramMap = jsonMapper.toMap(stateData.getMapParams());
        CreateAlertRequest createAlertRequest = new CreateAlertRequest();
        createAlertRequest.setAlertId(stateData.getAlertId());
        createAlertRequest.setUserId(String.valueOf(userId));
        List<ParameterInfo> parameterInfos =
                paramMap.entrySet().stream()
                        .flatMap(entry -> {
                            ParametersData parametersData =
                                    parametersDao.getByAlertIdAndParamName(stateData.getAlertId(), entry.getKey());
                            return entry.getValue().stream()
                                    .map(value -> {
                                        ParameterInfo parameterInfo = new ParameterInfo();
                                        parameterInfo.setId(parametersData.getParamId());
                                        parameterInfo.setValue(value);
                                        return parameterInfo;
                                    }).toList().stream();
                        }).toList();
        createAlertRequest.setParameters(parameterInfos);
        mayDayService.createAlert(createAlertRequest);
        SendMessage message = new SendMessage();
        message.setText(TextConstants.ALERT_CREATED.getText());
        message.setReplyMarkup(buildMainInlineKeyboardMarkup());
        log.info("Alert {} was created", createAlertRequest);
        return message;
    }

    public SendMessage createNextParameterRequest(String nextValue, StateData stateData) {
        ParametersData parametersData = parametersDao.getByAlertIdAndParamName(
                stateData.getAlertId(),
                nextValue);
        return ParamKeyboardBuilder.buildParamKeyboard(
                parametersData.getOptionsValues() != null,
                parametersData.getAlertId(),
                parametersData.getParamId(),
                parametersData.getParamName(),
                nextValue);
    }

    public SendMessage deleteAlert(long userId, String userAlertId) throws TException {
        log.info("Delete alert {} for user {}", userAlertId, userId);
        SendMessage message = new SendMessage();
        mayDayService.deleteAlert(String.valueOf(userId), userAlertId);
        message.setText(TextConstants.ALERT_REMOVED.getText());
        message.setReplyMarkup(buildMainInlineKeyboardMarkup());
        return message;
    }
}
