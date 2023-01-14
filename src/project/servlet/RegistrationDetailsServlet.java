package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dto.CreateUserDetailsDto;
import project.exception.ValidationException;
import project.service.UserDetailsService;
import project.util.JspHelper;

import java.io.IOException;
import java.time.LocalDateTime;

import static project.util.UrlPath.REGISTRATION_DETAILS;

@WebServlet(REGISTRATION_DETAILS)
public class RegistrationDetailsServlet extends HttpServlet {

    private final UserDetailsService userDetailsService = UserDetailsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("registration_details"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var userId = req.getSession().getAttribute("userId");
        var userDetailsDto = CreateUserDetailsDto.builder()
                .userId((Long) userId)
                .name(req.getParameter("name"))
                .surname(req.getParameter("surname"))
                .birthday(req.getParameter("birthday"))
                .phone(req.getParameter("phone"))
                .registrationDate(LocalDateTime.now())
                .build();
        try {
            userDetailsService.create(userDetailsDto);
            resp.sendRedirect("/login");
        } catch (ValidationException exception) {
            req.setAttribute("errors", exception.getErrors());
            req.setAttribute("userId", userId);
            doGet(req, resp);
        }
    }
}
