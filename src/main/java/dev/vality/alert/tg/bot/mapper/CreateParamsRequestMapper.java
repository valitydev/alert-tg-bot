package dev.vality.alert.tg.bot.mapper;

import dev.vality.alert.tg.bot.constants.TextConstants;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alert.tg.bot.model.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CreateParamsRequestMapper {

    private final StateDataDao stateDataDao;
    private final ParametersDao parametersDao;
    private final ReplyMessagesMapper replyMessagesMapper;
    private final JsonMapper jsonMapper;

    public SendMessage createRequest(Long userId, String paramName, String paramValue) throws TException {
        StateData stateData = stateDataDao.getByUserId(userId);
        List<Parameter> paramMap = jsonMapper.toMap(stateData.getMapParams());
        ParametersData parametersData = parametersDao.getByAlertIdAndParamName(stateData.getAlertId(), paramName);
        if (isParamValueMatchToProcess(parametersData, paramValue, paramMap)) {
            paramMap.stream()
                    .filter(parameter -> parameter.getName().equals(paramName))
                    .findFirst()
                    .orElseThrow()
                    .getValues().add(paramValue);
            stateData.setMapParams(jsonMapper.toJson(paramMap));
            stateDataDao.updateParams(userId, stateData.getMapParams());
        }
        String nextKey = getNextKeyForFill(paramMap, parametersData);
        if (nextKey != null) {
            return replyMessagesMapper.createNextParameterRequest(nextKey, stateData);
        } else {
            return replyMessagesMapper.createAlertRequest(userId);
        }
    }

    private boolean isParamValueMatchToProcess(ParametersData parametersData, String paramValue,
                                               List<Parameter> parameters) {
        return (!parametersData.getMandatory()
                && (isValuePattern(paramValue, parametersData)
                || paramValue.equals(TextConstants.EMPTY_PARAM.getText())))
                || (parametersData.getMandatory()
                && !paramValue.equals(TextConstants.EMPTY_PARAM.getText())
                && isValuePattern(paramValue, parametersData))
                || (parametersData.getMandatory()
                && paramValue.equals(TextConstants.EMPTY_PARAM.getText())
                && parameters.stream()
                .anyMatch(parameter -> parameter.getId().equals(parametersData.getParamId())
                        && !parameter.getValues().contains(TextConstants.EMPTY_PARAM.getText())));
    }

    private boolean isValuePattern(String value, ParametersData parametersData) {
        if (parametersData.getValueRegexp() != null) {
            Pattern pattern = Pattern.compile(parametersData.getValueRegexp());
            return pattern.matcher(value).matches();
        }
        return true;
    }

    private static String getNextKeyForFill(List<Parameter> parameters, ParametersData parametersData) {

        Parameter lastParameter =
                parameters.stream().filter(parameter -> parameter.getName().equals(parametersData.getParamName()))
                        .findFirst().orElseThrow();

        //Если параметр может принимать несколько значений и пользователь не ввёл символ прекращения ввода
        if (parametersData.getMultipleValues()
                && !lastParameter.getValues().contains(TextConstants.EMPTY_PARAM.getText())
                //Если параметр обязательный, но пользователь так и не передал значение
                || parametersData.getMandatory()
                && lastParameter.getValues().contains(TextConstants.EMPTY_PARAM.getText())
                && lastParameter.getValues().size() == 1) {
            return parametersData.getParamName();
        }
        return parameters.stream()
                .sorted()
                // Выбираем первый параметр без значений
                .filter(parameter -> parameter.getValues().isEmpty())
                .findFirst().map(Parameter::getName)
                .orElse(null);
    }
}
