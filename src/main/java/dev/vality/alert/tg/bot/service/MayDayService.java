package dev.vality.alert.tg.bot.service;

import dev.vality.alerting.mayday.*;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MayDayService {

    private final AlertingServiceSrv.Iface mayDayClient;

    public void deleteAllAlerts(String userId) throws TException {
        mayDayClient.deleteAllAlerts(userId);
    }

    public void deleteAlert(String userId, String userAlertId) throws TException {
        mayDayClient.deleteAlert(userId, userAlertId);
    }

    public List<UserAlert> getUserAlerts(String userId) throws TException {
        return mayDayClient.getUserAlerts(userId);
    }

    public List<Alert> getSupportedAlerts() throws TException {
        return mayDayClient.getSupportedAlerts();
    }

    public AlertConfiguration getAlertConfiguration(String alertId) throws TException {
        return mayDayClient.getAlertConfiguration(alertId);
    }

    public void createAlert(CreateAlertRequest createAlertRequest) throws TException {
        mayDayClient.createAlert(createAlertRequest);
    }
}
