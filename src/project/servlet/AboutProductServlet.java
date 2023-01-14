package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.service.ProductService;
import project.util.JspHelper;

import java.io.IOException;

import static project.util.UrlPath.ABOUT_PRODUCT;

@WebServlet(ABOUT_PRODUCT)
public class AboutProductServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        req.setAttribute("product", productService.getProductById(id));
        req.getRequestDispatcher(JspHelper.getPath("about_product"))
                .forward(req, resp);
    }
}
