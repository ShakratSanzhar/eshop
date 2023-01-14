package project.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dto.ReadUserDto;
import project.enums.Role;

import java.io.IOException;
import java.util.Set;

import static project.util.UrlPath.IMAGES;
import static project.util.UrlPath.LOCALE;
import static project.util.UrlPath.LOGIN;
import static project.util.UrlPath.NEW_CATEGORY;
import static project.util.UrlPath.NEW_PRODUCT;
import static project.util.UrlPath.REGISTRATION;
import static project.util.UrlPath.REGISTRATION_DETAILS;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Set<String> PUBLIC_PATH = Set.of(LOGIN, REGISTRATION, IMAGES, REGISTRATION_DETAILS, LOCALE);
    private static final Set<String> PRIVATE_PATH = Set.of(NEW_CATEGORY, NEW_PRODUCT);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var uri = ((HttpServletRequest) servletRequest).getRequestURI();
        if (isAdmin(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            if (isUserLoggedIn(servletRequest)) {
                if (isPrivatePath(uri)) {
                    ((HttpServletResponse) servletResponse).sendRedirect(LOGIN);
                } else {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            } else {
                if (isPublicPath(uri)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    ((HttpServletResponse) servletResponse).sendRedirect(LOGIN);
                }
            }
        }
    }

    private boolean isAdmin(ServletRequest servletRequest) {
        var user = (ReadUserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user != null && user.getRole() == Role.ADMINISTRATOR;
    }

    private boolean isPrivatePath(String uri) {
        return PRIVATE_PATH.stream().anyMatch(uri::startsWith);
    }

    private boolean isUserLoggedIn(ServletRequest servletRequest) {
        var user = (ReadUserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user != null;
    }

    private boolean isPublicPath(String uri) {
        return PUBLIC_PATH.stream().anyMatch(uri::startsWith);
    }
}
