package project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dto.CreateCartDto;
import project.dto.ReadUserDto;
import project.service.CartService;

import java.io.IOException;

import static project.util.UrlPath.ADD_PRODUCT_TO_CART;

@WebServlet(ADD_PRODUCT_TO_CART)
public class AddingProductToCartServlet extends HttpServlet {

    private final CartService cartService = CartService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String product = req.getParameter("product");
        String count = req.getParameter("count");
        var user = (ReadUserDto) req.getSession().getAttribute("user");
        if (req.getSession().getAttribute("cartId") == null) {
            var cart = CreateCartDto.builder()
                    .user(req.getSession().getAttribute("user"))
                    .price(0)
                    .build();
            Long cartId = cartService.create(cart);
            req.getSession().setAttribute("cartId", cartId);
        }
        Long cartId = (Long) req.getSession().getAttribute("cartId");
        req.getSession().setAttribute("successAddingToCart", cartService.addProductToCart(cartId, user.getId(), Long.parseLong(product), Integer.parseInt(count)));
        resp.sendRedirect("/cart");
    }
}
