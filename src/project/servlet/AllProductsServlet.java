package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dto.ProductFilter;
import project.dto.ReadProductDto;
import project.service.CategoryService;
import project.service.ProductService;
import project.util.JspHelper;

import java.io.IOException;
import java.util.List;

import static project.util.UrlPath.ALL_PRODUCTS;

@WebServlet(ALL_PRODUCTS)
public class AllProductsServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();
    private final CategoryService categoryService = CategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("category", req.getParameter("category"));
        req.setAttribute("name", req.getParameter("name"));
        req.setAttribute("author", req.getParameter("author"));
        req.setAttribute("publisher", req.getParameter("publisher"));
        req.setAttribute("price", req.getParameter("price"));
        req.setAttribute("categories", categoryService.findAllSubCategories());
        int page = 1;
        int recordsPerPage = 1;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }
        var productFilter = ProductFilter.builder()
                .limit(recordsPerPage)
                .offset((page - 1) * recordsPerPage)
                .categoryId(categoryService.getCategoryIdByName(req.getParameter("category")))
                .name(req.getParameter("name"))
                .author(req.getParameter("author"))
                .publisher(req.getParameter("publisher"))
                .price(req.getParameter("price"))
                .build();
        List<ReadProductDto> listOfProducts = productService.findProductsByFilter(productFilter);
        var filter = ProductFilter.builder()
                .limit(1000)
                .offset(0)
                .categoryId(categoryService.getCategoryIdByName(req.getParameter("category")))
                .name(req.getParameter("name"))
                .author(req.getParameter("author"))
                .publisher(req.getParameter("publisher"))
                .price(req.getParameter("price"))
                .build();
        int noOfRecords = productService.findProductsByFilter(filter).size();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        req.setAttribute("products", listOfProducts);
        req.setAttribute("noOfPages", noOfPages);
        req.setAttribute("currentPage", page);
        req.getRequestDispatcher(JspHelper.getPath("all_products"))
                .forward(req, resp);
    }
}
