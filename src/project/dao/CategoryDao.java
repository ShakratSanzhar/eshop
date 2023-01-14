package project.dao;

import project.dto.CategoryFilter;
import project.entity.Category;
import project.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDao implements Dao<Integer, Category> {

    private static final CategoryDao INSTANCE = new CategoryDao();
    private static final String DELETE_SQL = """
            DELETE FROM category
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO category (name, parent_id)
            VALUES (?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE category
            SET name =?,
                parent_id=?
            WHERE id =?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                name,
                parent_id
            FROM category
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE id=?
            """;

    private CategoryDao() {
    }

    public static CategoryDao getInstance() {
        return INSTANCE;
    }

    public List<Category> findAll(CategoryFilter filter) {
        List<Object> parameters = new ArrayList<>();
        String whereSql = "";
        if (filter.name() != null) {
            whereSql = "name LIKE ?";
            parameters.add("%" + filter.name() + "%");
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        var where = "WHERE" + whereSql + "LIMIT ? OFFSET ?";
        var sql = FIND_ALL_SQL + where;
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            var resultSet = preparedStatement.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                categories.add(buildCategory(resultSet));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category save(Category category) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, category.getName());
            if (category.getParentCategory() != null) {
                preparedStatement.setInt(2, category.getParentCategory().getId());
            } else {
                preparedStatement.setObject(2, null);
            }
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                category.setId(generatedKeys.getInt("id"));
            }
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Category category) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getParentCategory().getId());
            preparedStatement.setInt(3, category.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                categories.add(buildCategory(resultSet));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Category> findById(Integer id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            Category category = null;
            if (resultSet.next()) {
                category = buildCategory(resultSet);
            }
            return Optional.ofNullable(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Category> findById(Integer id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Category buildCategory(ResultSet resultSet) throws SQLException {
        return new Category(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                getInstance().findById(resultSet.getInt("parent_id"), resultSet.getStatement().getConnection()).orElse(null)
        );
    }
}
