package project.dao;

import project.entity.Blacklist;
import project.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlacklistDao {

    private static final BlacklistDao INSTANCE = new BlacklistDao();
    private static final String DELETE_SQL = """
            DELETE FROM blacklist
            WHERE user_id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO blacklist (user_id)
            VALUES (?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT user_id
            FROM blacklist
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE user_id=?
            """;
    private final UserDao userDao = UserDao.getInstance();

    private BlacklistDao() {
    }

    public static BlacklistDao getInstance() {
        return INSTANCE;
    }

    public List<Blacklist> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Blacklist> blacklists = new ArrayList<>();
            while (resultSet.next()) {
                blacklists.add(buildBlacklist(resultSet));
            }
            return blacklists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Blacklist> findById(Long userId) {
        try (var connection = ConnectionManager.get();
                var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, userId);
            var resultSet = preparedStatement.executeQuery();
            Blacklist blacklist = null;
            if (resultSet.next()) {
                blacklist = buildBlacklist(resultSet);
            }
            return Optional.ofNullable(blacklist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Blacklist save(Blacklist blacklist) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, blacklist.getUser().getId());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                blacklist.setUser(userDao.findById(generatedKeys.getLong("user_id"),generatedKeys.getStatement().getConnection()).orElse(null));
            }
            return blacklist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long userId) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Blacklist buildBlacklist(ResultSet resultSet) throws SQLException {
        return new Blacklist(
                userDao.findById(resultSet.getLong("user_id"),resultSet.getStatement().getConnection()).orElse(null)
        );
    }
}
