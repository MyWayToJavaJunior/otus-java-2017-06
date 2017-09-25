package listener;

import db_service.CachedUserDBService;
import hw10.dataset.AddressDataSet;
import hw10.dataset.PhoneDataSet;
import hw10.dataset.UserDataSet;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Arrays;

@WebListener
public class SeedListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        CachedUserDBService dbService = (CachedUserDBService) WebApplicationContextUtils
                .getWebApplicationContext(servletContextEvent.getServletContext()).getBean("cachedUserDBService");

        UserDataSet admin = new UserDataSet("Admin", "secret", 1, new AddressDataSet("Street"),
                Arrays.asList(new PhoneDataSet("+70001112200")));
        admin.setRole("ADMIN");
        dbService.save(admin);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
