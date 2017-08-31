package servlet;

import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    public final static String URL = "/login";
    final static String AUTH_ATTRIBUTE = "admin";
    private final static String PAGE = "login.html";

    private final DBService dbService;

    public LoginServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(TemplateProcessor.instance().getPage(PAGE, new HashMap<>()));

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDataSet user = dbService.get(req.getParameter("name"));
        if (user != null && user.getRole().equals("ADMIN") && UserDataSet.PASSWORD_ENCODER.matches(req.getParameter("password"), user.getPassword())) {
            req.getSession().setAttribute(AUTH_ATTRIBUTE, true);
            resp.sendRedirect(AdminServlet.URL);
        } else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("error", "Forbidden");
            resp.getWriter().println(TemplateProcessor.instance().getPage(PAGE, pageVariables));

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
