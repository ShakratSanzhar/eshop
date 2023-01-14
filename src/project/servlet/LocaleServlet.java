package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.util.UrlPath;

import java.io.IOException;

import static project.util.UrlPath.LOCALE;

@WebServlet(LOCALE)
public class LocaleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var language = req.getParameter("lang");
        req.getSession().setAttribute("lang", language);
        var prevPage = req.getHeader("referer");
        var page = prevPage != null ? prevPage : UrlPath.LOGIN;
        resp.sendRedirect(page + "?lang=" + language);
    }
}
