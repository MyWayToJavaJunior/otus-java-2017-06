package servlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class BaseServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        WebApplicationContextUtils.getWebApplicationContext(config.getServletContext())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }
}
