package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import project.dto.ReadUserDto;
import project.service.UserService;
import project.util.JspHelper;

import java.io.IOException;

import static project.util.UrlPath.LOGIN;

@WebServlet(LOGIN)
public class LoginServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("login"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService.login(req.getParameter("email"), req.getParameter("password"))
                .ifPresentOrElse(
                        readUserDto -> onLoginSuccess(readUserDto, req, resp),
                        () -> onLoginFail(req, resp)
                );
    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        resp.sendRedirect("/login?error&email=" + req.getParameter("email"));
    }

    @SneakyThrows
    private void onLoginSuccess(ReadUserDto readUserDto, HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("user", readUserDto);
        resp.sendRedirect("/all-products");
    }
}
