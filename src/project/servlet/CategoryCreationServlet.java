package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dto.CreateCategoryDto;
import project.exception.ValidationException;
import project.service.CategoryService;
import project.util.JspHelper;

import java.io.IOException;

import static project.util.UrlPath.NEW_CATEGORY;

@WebServlet(NEW_CATEGORY)
public class CategoryCreationServlet extends HttpServlet {

    private final CategoryService categoryService = CategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", categoryService.findAllCategories());
        req.getRequestDispatcher(JspHelper.getPath("category_creation"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var categoryDto = CreateCategoryDto.builder()
                .name(req.getParameter("name"))
                .parentCategory(req.getParameter("parentCategory"))
                .build();
        try {
            categoryService.create(categoryDto);
            resp.sendRedirect("/all-products");
        } catch (ValidationException exception) {
            req.setAttribute("errors", exception.getErrors());
            doGet(req, resp);
        }
    }
}
