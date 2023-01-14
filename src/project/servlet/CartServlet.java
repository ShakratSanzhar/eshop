package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dto.ProductsInCartDto;
import project.entity.Cart;
import project.service.CartService;
import project.util.JspHelper;

import java.io.IOException;
import java.util.List;

import static project.util.UrlPath.CART;

@WebServlet(CART)
public class CartServlet extends HttpServlet {

    private final CartService cartService = CartService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getSession().getAttribute("cartId") == null) {
            resp.sendRedirect("/all-products");
        } else {
            Long cartId = (Long) req.getSession().getAttribute("cartId");
            List<ProductsInCartDto> productsInCart = cartService.getAllProductsInCart(cartId);
            Cart cart = cartService.getCartById(cartId).get();
            req.setAttribute("products", productsInCart);
            req.setAttribute("cart", cart);
            if (req.getSession().getAttribute("successAddingToCart") != null) {
                req.setAttribute("success", req.getSession().getAttribute("successAddingToCart"));
                req.getSession().removeAttribute("successAddingToCart");
            }
            req.getRequestDispatcher(JspHelper.getPath("cart"))
                    .forward(req, resp);
        }
    }
}
