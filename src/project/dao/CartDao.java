package project.dao;

import project.dto.CartFilter;
import project.dto.CartProductDto;
import project.dto.ProductsInCartDto;
import project.entity.Cart;
import project.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class CartDao implements Dao<Long, Cart> {

    private static final CartDao INSTANCE = new CartDao();
    private static final String DELETE_SQL = """
            DELETE FROM cart
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO cart (user_id, price)
            VALUES (?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE cart
            SET user_id =?,
                price=?
            WHERE id =?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                user_id,
                price
            FROM cart
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE id=?
            """;
    private static final String ADD_PRODUCT_TO_CART = """
            INSERT INTO cart_product (cart_id, product_id, quantity)
            VALUES (?, ?, ?)
            """;
    private static final String CHANGE_PRODUCT_QUANTITY_IN_CART = """
            UPDATE cart_product
            SET quantity = ?
            WHERE cart_id = ? AND 
            product_id = ?
            """;
    private static final String GET_ALL_PRODUCTS_IN_CART = """
            SELECT product_id,
            quantity
            FROM cart_product
            WHERE cart_id = ?
            """;
    private final UserDao userDao = UserDao.getInstance();
    private final ProductDao productDao = ProductDao.getInstance();

    private CartDao() {
    }

    public static CartDao getInstance() {
        return INSTANCE;
    }

    public List<ProductsInCartDto> getAllProductsInCart(Long cartId) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_ALL_PRODUCTS_IN_CART)) {
            preparedStatement.setLong(1, cartId);
            var resultSet = preparedStatement.executeQuery();
            List<ProductsInCartDto> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(new ProductsInCartDto(productDao.findById(resultSet.getLong("product_id"), resultSet.getStatement().getConnection()).orElse(null),
                        resultSet.getInt("quantity")));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeProductQuantityInCart(CartProductDto cartProductDto) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(CHANGE_PRODUCT_QUANTITY_IN_CART)) {
            preparedStatement.setInt(1, cartProductDto.getQuantity());
            preparedStatement.setLong(2, cartProductDto.getCart().getId());
            preparedStatement.setLong(3, cartProductDto.getProduct().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CartProductDto addProductToCart(CartProductDto cartProductDto) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(ADD_PRODUCT_TO_CART, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, cartProductDto.getCart().getId());
            preparedStatement.setLong(2, cartProductDto.getProduct().getId());
            preparedStatement.setInt(3, cartProductDto.getQuantity());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                cartProductDto.setCart(findById(generatedKeys.getLong("cart_id"), generatedKeys.getStatement().getConnection()).orElse(null));
                cartProductDto.setProduct(productDao.findById(generatedKeys.getLong("product_id"), generatedKeys.getStatement().getConnection()).orElse(null));
                cartProductDto.setQuantity(generatedKeys.getInt("quantity"));
            }
            return cartProductDto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cart> findAll(CartFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (filter.user() != null) {
            whereSql.add("user_id = ?");
            parameters.add(filter.user().getId());
        }
        if (filter.price() != null) {
            whereSql.add("price = ?");
            parameters.add(filter.price());
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        var where = whereSql.stream()
                .collect(joining("AND", "WHERE", "LIMIT ? OFFSET ? "));
        var sql = FIND_ALL_SQL + where;
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            var resultSet = preparedStatement.executeQuery();
            List<Cart> carts = new ArrayList<>();
            while (resultSet.next()) {
                carts.add(buildCart(resultSet));
            }
            return carts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cart> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Cart> carts = new ArrayList<>();
            while (resultSet.next()) {
                carts.add(buildCart(resultSet));
            }
            return carts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Cart> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Cart cart = null;
            if (resultSet.next()) {
                cart = buildCart(resultSet);
            }
            return Optional.ofNullable(cart);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Cart> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Cart cart) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setLong(1, cart.getUser().getId());
            preparedStatement.setInt(2, cart.getPrice());
            preparedStatement.setLong(3, cart.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Cart save(Cart cart) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, cart.getUser().getId());
            preparedStatement.setInt(2, cart.getPrice());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                cart.setId(generatedKeys.getLong("id"));
            }
            return cart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Cart buildCart(ResultSet resultSet) throws SQLException {
        return new Cart(
                resultSet.getLong("id"),
                userDao.findById(resultSet.getLong("user_id"), resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getInt("price")
        );
    }
}
