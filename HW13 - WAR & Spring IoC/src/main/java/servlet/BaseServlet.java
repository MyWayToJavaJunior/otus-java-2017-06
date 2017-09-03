package servlet;

import context.Context;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class BaseServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        Context.instance().getAutowireCapableBeanFactory().autowireBean(this);
    }
}
