package project.dao;

import project.dto.OrderFilter;
import project.dto.OrderProductDto;
import project.dto.ProductsInCartDto;
import project.entity.Order;
import project.enums.OrderStatus;
import project.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class OrderDao implements Dao<Long, Order> {

    private static final OrderDao INSTANCE = new OrderDao();
    private static final String DELETE_SQL = """
            DELETE FROM orders
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO orders (user_id, status, price, created_at, closed_at)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE orders
            SET user_id =?,
                status=?,
                price=?,
                created_at=?,
                closed_at=?
            WHERE id =?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                user_id,
                status,
                price,
                created_at,
                closed_at
            FROM orders
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE id=?
            """;
    private static final String ADD_PRODUCT_TO_ORDER = """
            INSERT INTO order_product (order_id, product_id, quantity)
            VALUES (?, ?, ?)
            """;
    private static final String CHANGE_PRODUCT_QUANTITY_IN_ORDER = """
            UPDATE order_product
            SET quantity = ?
            WHERE order_id = ? AND product_id = ?
            """;
    private static final String GET_ALL_PRODUCTS_IN_ORDER = """
            SELECT product_id,
            quantity
            FROM order_product
            WHERE order_id = ?
            """;
    private final UserDao userDao = UserDao.getInstance();
    private final ProductDao productDao = ProductDao.getInstance();

    private OrderDao() {
    }

    public static OrderDao getInstance() {
        return INSTANCE;
    }

    /**
     * Return all products in order.
     *
     * @param orderId ID of the order
     * @return List of ProductDto
     */
    public List<ProductsInCartDto> getAllProductsInOrder(Long orderId) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_ALL_PRODUCTS_IN_ORDER)) {
            preparedStatement.setLong(1, orderId);
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

    public void changeProductQuantityInOrder(OrderProductDto orderProductDto) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(CHANGE_PRODUCT_QUANTITY_IN_ORDER)) {
            preparedStatement.setInt(1, orderProductDto.getQuantity());
            preparedStatement.setLong(2, orderProductDto.getOrder().getId());
            preparedStatement.setLong(3, orderProductDto.getProduct().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public OrderProductDto addProductToOrder(OrderProductDto orderProductDto) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(ADD_PRODUCT_TO_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, orderProductDto.getOrder().getId());
            preparedStatement.setLong(2, orderProductDto.getProduct().getId());
            preparedStatement.setInt(3, orderProductDto.getQuantity());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderProductDto.setOrder(findById(generatedKeys.getLong("order_id"), generatedKeys.getStatement().getConnection()).orElse(null));
                orderProductDto.setProduct(productDao.findById(generatedKeys.getLong("product_id"), generatedKeys.getStatement().getConnection()).orElse(null));
                orderProductDto.setQuantity(generatedKeys.getInt("quantity"));
            }
            return orderProductDto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> findAll(OrderFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (filter.user() != null) {
            whereSql.add("user_id = ?");
            parameters.add(filter.user().getId());
        }
        if (filter.status() != null) {
            whereSql.add("status = ?");
            parameters.add(filter.status().name());
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
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(buildOrder(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(buildOrder(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Order> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Order order = null;
            if (resultSet.next()) {
                order = buildOrder(resultSet);
            }
            return Optional.ofNullable(order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Order> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Order order) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setLong(1, order.getUser().getId());
            preparedStatement.setString(2, order.getStatus().name());
            preparedStatement.setInt(3, order.getPrice());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(order.getClosedAt()));
            preparedStatement.setLong(6, order.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order save(Order order) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, order.getUser().getId());
            preparedStatement.setString(2, order.getStatus().name());
            preparedStatement.setInt(3, order.getPrice());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(order.getClosedAt()));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setId(generatedKeys.getLong("id"));
            }
            return order;
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

    private Order buildOrder(ResultSet resultSet) throws SQLException {
        return new Order(
                resultSet.getLong("id"),
                userDao.findById(resultSet.getLong("user_id"), resultSet.getStatement().getConnection()).orElse(null),
                OrderStatus.valueOf(resultSet.getString("status")),
                resultSet.getInt("price"),
                resultSet.getTimestamp("created_at").toLocalDateTime(),
                resultSet.getTimestamp("closed_at").toLocalDateTime()
        );
    }
}
