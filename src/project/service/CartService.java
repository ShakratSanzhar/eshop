package project.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import project.dao.CartDao;
import project.dao.ProductDao;
import project.dao.UserDao;
import project.dto.CartProductDto;
import project.dto.CreateCartDto;
import project.dto.ProductsInCartDto;
import project.dto.ReadUserDto;
import project.entity.Cart;
import project.entity.Product;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartService {

    private static final CartService INSTANCE = new CartService();
    private final UserDao userDao = UserDao.getInstance();
    private final CartDao cartDao = CartDao.getInstance();
    private final ProductDao productDao = ProductDao.getInstance();

    public Long create(CreateCartDto cartDto) {
        var cartEntity = Cart.builder()
                .user(userDao.findById(((ReadUserDto) cartDto.getUser()).getId()).get())
                .price(cartDto.getPrice())
                .build();
        cartDao.save(cartEntity);
        return cartEntity.getId();
    }

    public Optional<Cart> getCartById(Long id) {
        return cartDao.findById(id);
    }

    public List<ProductsInCartDto> getAllProductsInCart(Long cartId) {
        return cartDao.getAllProductsInCart(cartId);
    }

    public static CartService getInstance() {
        return INSTANCE;
    }

    public String addProductToCart(Long cartId, Long userId, Long productId, Integer count) {
        Product product = productDao.findById(productId).get();
        Cart cart = getCartById(cartId).get();
        Integer prevPrice = cart.getPrice();
        if (count > product.getRemainingAmount()) {
            return "Столько книг: " + product.getName() + " нет на складе брат";
        }
        var cartProductDto = CartProductDto.builder()
                .cart(cart)
                .product(productDao.findById(productId).get())
                .quantity(count)
                .build();
        cartDao.addProductToCart(cartProductDto);
        cart.setPrice(prevPrice + count * product.getPrice());
        cartDao.update(cart);
        return "Книги: " + product.getName() + " успешно добавлены в корзину";
    }
}
