package listener;

import db_service.CachedUserDBService;
import message_system.Address;
import message_system.MessageSystem;
import message_system.MessageSystemContext;
import message_system.addressee.FrontendService;
import message_system.addressee.MessageSystemDBService;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MessageSystemListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        MessageSystem messageSystem = (MessageSystem) WebApplicationContextUtils
                .getWebApplicationContext(servletContextEvent.getServletContext())
                .getBean("messageSystem");

        MessageSystemContext context = (MessageSystemContext) WebApplicationContextUtils
                .getWebApplicationContext(servletContextEvent.getServletContext())
                .getBean("messageSystemContext");

        CachedUserDBService cachedUserDBService = (CachedUserDBService) WebApplicationContextUtils
                .getWebApplicationContext(servletContextEvent.getServletContext())
                .getBean("cachedUserDBService");

        MessageSystemDBService messageSystemDBService = new MessageSystemDBService(cachedUserDBService);
        messageSystem.addAddressee(messageSystemDBService);
        context.setDbAddress(messageSystemDBService.getAddress());

        FrontendService frontendService = new FrontendService();
        messageSystem.addAddressee(frontendService);
        context.setFrontAddress(frontendService.getAddress());

        messageSystem.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
