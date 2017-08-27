package servlet;

import cache.CacheImpl;
import db_service.CachedUserDBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet {
    public final static String URL = "/admin";
    private final static String PAGE = "admin.html";

    private final CachedUserDBService cachedUserDBService;

    public AdminServlet(CachedUserDBService cachedUserDBService) {
        this.cachedUserDBService = cachedUserDBService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute(LoginServlet.AUTH_ATTRIBUTE) == null || req.getSession().getAttribute(LoginServlet.AUTH_ATTRIBUTE).equals(false)) {
            resp.sendRedirect(LoginServlet.URL);
            return;
        }

        CacheImpl cache = (CacheImpl) cachedUserDBService.getCache();

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("hit", cache.getHitCount());
        pageVariables.put("miss", cache.getMissCount());
        pageVariables.put("max_size", cache.getMaxElements());
        pageVariables.put("cur_size", cache.getCurCacheSize());
        pageVariables.put("life_time", cache.getLifeTimeMs());
        pageVariables.put("idle_time", cache.getIdleTimeMs());
        pageVariables.put("eternal", "" + cache.isEternal());


        resp.getWriter().println(TemplateProcessor.instance().getPage(PAGE, pageVariables));

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
