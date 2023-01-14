package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dto.CreateUserDto;
import project.enums.Role;
import project.exception.ValidationException;
import project.service.UserDetailsService;
import project.service.UserService;
import project.util.JspHelper;

import java.io.IOException;

import static project.util.UrlPath.REGISTRATION;

@WebServlet(REGISTRATION)
public class RegistrationServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final UserDetailsService userDetailsService = UserDetailsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("roles", Role.values());
        req.getRequestDispatcher(JspHelper.getPath("registration"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var userDto = CreateUserDto.builder()
                .username(req.getParameter("username"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .role(req.getParameter("role"))
                .build();
        try {
            var userId = userService.create(userDto);
            req.getSession().setAttribute("userId", userId);
            req.getRequestDispatcher(JspHelper.getPath("registration_details"))
                    .forward(req, resp);
        } catch (ValidationException exception) {
            req.setAttribute("errors", exception.getErrors());
            doGet(req, resp);
        }
    }
}
