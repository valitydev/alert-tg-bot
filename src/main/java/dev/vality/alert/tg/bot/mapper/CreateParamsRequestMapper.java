package dev.vality.alert.tg.bot.mapper;

import dev.vality.alert.tg.bot.constants.TextConstants;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
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
        Map<String, Set<String>> paramMap = jsonMapper.toMap(stateData.getMapParams());
        ParametersData parametersData = parametersDao.getByAlertIdAndParamName(stateData.getAlertId(), paramName);
        if (isParamValueMatchToProcess(parametersData, paramValue, paramMap)) {
            Set<String> values = new HashSet<>();
            values.add(paramValue);

            if (!paramMap.containsKey(paramName)) {
                paramMap.put(paramName, values);
            } else {
                paramMap.get(paramName).addAll(values);
            }

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

    private boolean isParamValueMatchToProcess(ParametersData parametersData, String paramValue, Map<String,
            Set<String>> providedParams) {
        return (!parametersData.getMandatory()
                && (isValuePattern(paramValue, parametersData)
                || paramValue.equals(TextConstants.EMPTY_PARAM.getText())))
                || (parametersData.getMandatory()
                && !paramValue.equals(TextConstants.EMPTY_PARAM.getText())
                && isValuePattern(paramValue, parametersData))
                || (parametersData.getMandatory()
                && paramValue.equals(TextConstants.EMPTY_PARAM.getText())
                && providedParams.containsKey(parametersData.getParamId()));
    }

    private boolean isValuePattern(String value, ParametersData parametersData) {
        if (parametersData.getValueRegexp() != null) {
            Pattern pattern = Pattern.compile(parametersData.getValueRegexp());
            return pattern.matcher(value).matches();
        }
        return true;
    }

    private static String getNextKeyForFill(Map<String, Set<String>> map, ParametersData parametersData) {
        return map.entrySet().stream()
                // Если у параметра вообще нет значения
                .filter(entry -> null == entry.getValue()
                        //Если у параметр может принимать несколько значений, а
                        // пользователь продолжает вводить новые значения
                        || !entry.getValue().contains(TextConstants.EMPTY_PARAM.getText())
                        && parametersData.getMultipleValues()
                        //Если параметр обязательный, но пользователь передал только пустое значение
                        || entry.getValue().contains(TextConstants.EMPTY_PARAM.getText())
                        && parametersData.getMandatory() && entry.getValue().size() == 1)
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }
}
