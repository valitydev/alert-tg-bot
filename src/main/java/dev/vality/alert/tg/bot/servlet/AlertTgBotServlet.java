package dev.vality.alert.tg.bot.servlet;

import dev.vality.alerting.tg_bot.NotifierServiceSrv;
import dev.vality.woody.thrift.impl.http.THServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/alert/tg/bot")
public class AlertTgBotServlet extends GenericServlet {

    @Autowired
    private NotifierServiceSrv.Iface serverHandler;

    private Servlet servlet;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        servlet = new THServiceBuilder().build(NotifierServiceSrv.Iface.class, serverHandler);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        servlet.service(request, response);
    }
}
