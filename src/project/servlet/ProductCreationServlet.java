package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dto.CreateProductDto;
import project.exception.ValidationException;
import project.service.CategoryService;
import project.service.ProductService;
import project.util.JspHelper;

import java.io.IOException;
import java.time.LocalDateTime;

import static project.util.UrlPath.NEW_PRODUCT;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(NEW_PRODUCT)
public class ProductCreationServlet extends HttpServlet {

    private final CategoryService categoryService = CategoryService.getInstance();
    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", categoryService.findAllCategories());
        req.getRequestDispatcher(JspHelper.getPath("product_creation"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var productDto = CreateProductDto.builder()
                .category(req.getParameter("category"))
                .name(req.getParameter("name"))
                .description(req.getParameter("description"))
                .author(req.getParameter("author"))
                .publisher(req.getParameter("publisher"))
                .publishingYear(req.getParameter("publishingYear"))
                .image(req.getPart("image"))
                .price(req.getParameter("price"))
                .remainingAmount(req.getParameter("remainingAmount"))
                .pageCount(req.getParameter("pageCount"))
                .createdAt(LocalDateTime.now())
                .build();
        try {
            productService.create(productDto);
            resp.sendRedirect("/all-products");
        } catch (ValidationException exception) {
            req.setAttribute("errors", exception.getErrors());
            doGet(req, resp);
        }
    }
}
