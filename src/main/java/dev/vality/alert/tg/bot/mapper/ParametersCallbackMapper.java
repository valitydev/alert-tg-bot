package dev.vality.alert.tg.bot.mapper;

import dev.vality.alert.tg.bot.constants.TextConstants;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alert.tg.bot.model.Parameter;
import dev.vality.alert.tg.bot.service.MayDayService;
import dev.vality.alert.tg.bot.utils.ParamKeyboardBuilder;
import dev.vality.alerting.mayday.AlertConfiguration;
import dev.vality.alerting.mayday.ParameterConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;

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
        List<Parameter> sortedParams = fillStateDataAndSave(userId, alertId, parameterConfigurations);
        parameterConfigurations.forEach(param -> convertParameterConfigurationsAndSave(alertId, param));
        return ParamKeyboardBuilder.buildParamKeyboard(
                parameterConfigurations.stream()
                        .filter(parameterConfiguration ->
                                parameterConfiguration.getId().equals(sortedParams.get(0).getId()))
                        .findFirst()
                        .orElseThrow().isSetOptions(),
                alertId,
                sortedParams.get(0).getId(),
                sortedParams.get(0).getName(),
                sortedParams.get(0).getName());
    }

    private List<Parameter> fillStateDataAndSave(long userId, String alertId,
                                                 List<ParameterConfiguration> parameterConfigurations) {
        StateData stateData = stateDataDao.getByUserId(userId);
        stateData.setAlertId(alertId);
        List<Parameter> parameters = new ArrayList<>();
        parameterConfigurations.forEach(param -> {
            Parameter parameter = new Parameter();
            parameter.setId(param.getId());
            parameter.setName(param.getName());
            parameters.add(parameter);
        });
        parameters.sort(Parameter::compareTo);
        stateData.setMapParams(jsonMapper.toJson(parameters));
        stateDataDao.save(stateData);
        return parameters;
    }

    private void convertParameterConfigurationsAndSave(String alertId, ParameterConfiguration param) {
        ParametersData parametersData = new ParametersData();
        parametersData.setAlertId(alertId);
        parametersData.setParamId(param.getId());
        parametersData.setParamName(param.getName());
        Set<String> options = param.isSetOptions() ? new HashSet<>(param.getOptions()) : null;
        if (options != null && (!param.isMandatory() || param.isMultipleValues())) {
            options.add(TextConstants.EMPTY_PARAM.getText());
        }
        String optionsValues = jsonMapper.toJson(options);
        parametersData.setOptionsValues(optionsValues);
        parametersData.setMultipleValues(param.isMultipleValues());
        parametersData.setMandatory(param.isMandatory());
        parametersData.setValueRegexp(param.getValueRegexp());
        parametersDao.save(parametersData);
    }

}
