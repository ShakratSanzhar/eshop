package project.dao;

import project.dto.ProductFilter;
import project.entity.Product;
import project.util.ConnectionManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class ProductDao implements Dao<Long, Product> {

    private static final ProductDao INSTANCE = new ProductDao();
    private static final String DELETE_SQL = """
            DELETE FROM product
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO product(category_id, name, description, author, publisher, publishing_year, image, price, remaining_amount, page_count, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE product
            SET category_id =?,
                name=?,
                description=?,
                author=?,
                publisher=?,
                publishing_year=?,
                image=?,
                price=?,
                remaining_amount=?,
                page_count=?,
                created_at=?
            WHERE id =?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                category_id,
                name,
                description,
                author,
                publisher,
                publishing_year,
                image,
                price,
                remaining_amount,
                page_count,
                created_at
            FROM product
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE id=?
            """;
    private final CategoryDao categoryDao = CategoryDao.getInstance();

    private ProductDao() {
    }

    public static ProductDao getInstance() {
        return INSTANCE;
    }

    public void update(Product product) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, product.getCategory().getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setString(4, product.getAuthor());
            preparedStatement.setString(5, product.getPublisher());
            preparedStatement.setDate(6, Date.valueOf(product.getPublishingYear()));
            preparedStatement.setString(7, product.getImage());
            preparedStatement.setInt(8, product.getPrice());
            preparedStatement.setInt(9, product.getRemainingAmount());
            preparedStatement.setInt(10, product.getPageCount());
            preparedStatement.setTimestamp(11, Timestamp.valueOf(product.getCreatedAt()));
            preparedStatement.setLong(12, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product save(Product product) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, product.getCategory().getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setString(4, product.getAuthor());
            preparedStatement.setString(5, product.getPublisher());
            preparedStatement.setDate(6, Date.valueOf(product.getPublishingYear()));
            preparedStatement.setString(7, product.getImage());
            preparedStatement.setInt(8, product.getPrice());
            preparedStatement.setInt(9, product.getRemainingAmount());
            preparedStatement.setInt(10, product.getPageCount());
            preparedStatement.setTimestamp(11, Timestamp.valueOf(product.getCreatedAt()));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                product.setId(generatedKeys.getLong("id"));
            }
            return product;
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

    public List<Product> findAll(ProductFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (filter.getCategoryId() != null) {
            whereSql.add("category_id = ?");
            parameters.add(filter.getCategoryId());
        }
        if (!"".equals(filter.getName()) && filter.getName() != null) {
            whereSql.add("name LIKE ?");
            parameters.add("%" + filter.getName() + "%");
        }
        if (!"".equals(filter.getAuthor()) && filter.getAuthor() != null) {
            whereSql.add("author LIKE ?");
            parameters.add("%" + filter.getAuthor() + "%");
        }
        if (!"".equals(filter.getPublisher()) && filter.getPublisher() != null) {
            whereSql.add("publisher LIKE ?");
            parameters.add("%" + filter.getPublisher() + "%");
        }
        if (!"".equals(filter.getPrice()) && filter.getPrice() != null) {
            whereSql.add("price < ?");
            parameters.add(Integer.parseInt(filter.getPrice()));
        }
        parameters.add(filter.getLimit());
        parameters.add(filter.getOffset());
        String where = "";
        if (whereSql.isEmpty()) {
            where = "LIMIT ? OFFSET ? ";
        } else if (whereSql.size() == 1) {
            where = "WHERE " + whereSql.get(0) + " LIMIT ? OFFSET ? ";
        } else {
            where = whereSql.stream()
                    .collect(joining(" AND ", "WHERE ", "LIMIT ? OFFSET ? "));
        }
        var sql = FIND_ALL_SQL + where;
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            var resultSet = preparedStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(buildProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(buildProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Product product = null;
            if (resultSet.next()) {
                product = buildProduct(resultSet);
            }
            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Product buildProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getLong("id"),
                categoryDao.findById(resultSet.getInt("category_id"), resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("author"),
                resultSet.getString("publisher"),
                resultSet.getDate("publishing_year").toLocalDate(),
                resultSet.getString("image"),
                resultSet.getInt("price"),
                resultSet.getInt("remaining_amount"),
                resultSet.getInt("page_count"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

