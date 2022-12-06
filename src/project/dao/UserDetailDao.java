package project.dao;

import project.dto.UserDetailFilter;
import project.entity.UserDetail;
import project.util.ConnectionManager;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class UserDetailDao implements Dao<Long,UserDetail> {

    private static final UserDetailDao INSTANCE = new UserDetailDao();
    private static final String DELETE_SQL = """
            DELETE FROM user_details
            WHERE user_id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO user_details (user_id, name, surname, birthday, phone, registration_date)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE user_details
            SET name =?,
                surname=?,
                birthday=?,
                phone=?,
                registration_date=?
            WHERE user_id =?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT user_id,
                name,
                surname,
                birthday,
                phone,
                registration_date
            FROM user_details
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE user_id=?
            """;
    private final UserDao userDao = UserDao.getInstance();

    private UserDetailDao() {
    }

    public List<UserDetail> findAll(UserDetailFilter filter) {
        List<Object> parameters=new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if(filter.name()!=null) {
            whereSql.add("name LIKE ?");
            parameters.add("%" +filter.name()+"%");
        }
        if(filter.surname()!=null) {
            whereSql.add("surname LIKE ?");
            parameters.add("%" +filter.surname()+"%");
        }
        if(filter.birthday()!=null) {
            whereSql.add("birthday = ?");
            parameters.add(filter.birthday());
        }
        if(filter.phone()!=null) {
            whereSql.add("phone LIKE ?");
            parameters.add("%" +filter.phone()+"%");
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        var where = whereSql.stream()
                .collect(joining("AND","WHERE","LIMIT ? OFFSET ? "));
        var sql =FIND_ALL_SQL+ where;
        try(var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i+1,parameters.get(i));
            }
            var resultSet=preparedStatement.executeQuery();
            List<UserDetail> userDetails = new ArrayList<>();
            while (resultSet.next()) {
                userDetails.add(buildUserDetail(resultSet));
            }
            return userDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserDetail> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<UserDetail> userDetails = new ArrayList<>();
            while (resultSet.next()) {
                userDetails.add(buildUserDetail(resultSet));
            }
            return userDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<UserDetail> findById(Long userId) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, userId);
            var resultSet = preparedStatement.executeQuery();
            UserDetail userDetail = null;
            if (resultSet.next()) {
                userDetail = buildUserDetail(resultSet);
            }
            return Optional.ofNullable(userDetail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(UserDetail userDetail) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, userDetail.getName());
            preparedStatement.setString(2, userDetail.getSurname());
            preparedStatement.setDate(3, Date.valueOf(userDetail.getBirthday()));
            preparedStatement.setString(4, userDetail.getPhone());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(userDetail.getRegistrationDate()));
            preparedStatement.setLong(6, userDetail.getUser().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDetail save(UserDetail userDetail) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, userDetail.getUser().getId());
            preparedStatement.setString(2, userDetail.getName());
            preparedStatement.setString(3, userDetail.getSurname());
            preparedStatement.setDate(4, Date.valueOf(userDetail.getBirthday()));
            preparedStatement.setString(5, userDetail.getPhone());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(userDetail.getRegistrationDate()));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                userDetail.setUser(userDao.findById(generatedKeys.getLong("user_id"),generatedKeys.getStatement().getConnection()).orElse(null));
            }
            return userDetail;
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

    public static UserDetailDao getInstance() {
        return INSTANCE;
    }

    private UserDetail buildUserDetail(ResultSet resultSet) throws SQLException {
        return new UserDetail(
                userDao.findById(resultSet.getLong("user_id"),resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getDate("birthday").toLocalDate(),
                resultSet.getString("phone"),
                resultSet.getTimestamp("registration_date").toLocalDateTime()
        );
    }
}
