package dev.vality.alert.tg.bot.mapper;

import dev.vality.alert.tg.bot.constants.TextConstants;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alert.tg.bot.service.MayDayService;
import dev.vality.alert.tg.bot.utils.ParamKeyboardBuilder;
import dev.vality.alerting.mayday.AlertConfiguration;
import dev.vality.alerting.mayday.ParameterConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ParametersCallbackMapper {

    private final MayDayService mayDayService;
    private final StateDataDao stateDataDao;
    private final ParametersDao parametersDao;
    private final JsonMapper jsonMapper;

    public SendMessage mapParametersCallback(String callData, long userId) throws TException {
        AlertConfiguration alertConfiguration = mayDayService.getAlertConfiguration(callData);
        String alertId = alertConfiguration.getId();
        List<ParameterConfiguration> parameterConfigurations = alertConfiguration.getParameters();
        fillStateDataAndSave(userId, alertId, parameterConfigurations);
        parameterConfigurations.forEach(param -> convertParameterConfigurationsAndSave(alertId, param));
        return ParamKeyboardBuilder.buildParamKeyboard(
                parameterConfigurations.get(0).isSetOptions(),
                alertId,
                parameterConfigurations.get(0).getId(),
                parameterConfigurations.get(0).getName(),
                parameterConfigurations.get(0).getName());
    }

    private void fillStateDataAndSave(long userId, String alertId,
                                      List<ParameterConfiguration> parameterConfigurations) {
        StateData stateData = stateDataDao.getByUserId(userId);
        stateData.setAlertId(alertId);
        Map<String, String> mapParams = new HashMap<>();
        parameterConfigurations.forEach(param -> mapParams.put(param.getName(), null));
        stateData.setMapParams(jsonMapper.toJson(mapParams));
        stateDataDao.save(stateData);
    }

    private void convertParameterConfigurationsAndSave(String alertId, ParameterConfiguration param) {
        ParametersData parametersData = new ParametersData();
        parametersData.setAlertId(alertId);
        parametersData.setParamId(param.getId());
        parametersData.setParamName(param.getName());
        List<String> options = param.isSetOptions() ? new ArrayList<>(param.getOptions()) : null;
        if (options != null && !param.isMandatory()) {
            options.add(TextConstants.EMPTY_PARAM.getText());
        }
        String optionsValues = jsonMapper.toJson(options);
        parametersData.setOptionsValues(optionsValues);
        parametersData.setMandatory(param.isMandatory());
        parametersData.setValueRegexp(param.getValueRegexp());
        parametersDao.save(parametersData);
    }

}
