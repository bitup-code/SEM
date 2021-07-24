package todoapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import todoapp.model.User;
import todoapp.utils.JDBCUtils;

public class UserDao {

	public int registerEmployee(User employee) throws ClassNotFoundException {
		String INSERT_USERS_SQL = "INSERT INTO users"
				+ "  (first_name, last_name, username, password) VALUES "
				+ " (?, ?, ?, ?);";

		int result = 0;
		try (Connection connection = JDBCUtils.getConnection();

			 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, employee.getFirstName());
			preparedStatement.setString(2, employee.getLastName());
			preparedStatement.setString(3, employee.getUsername());
			preparedStatement.setString(4, employee.getPassword());

			System.out.println(preparedStatement);

			result = preparedStatement.executeUpdate();

		} catch (SQLException e) {

			JDBCUtils.printSQLException(e);
		}
		return result;
	}

	public User getByUsername(String username) {
		return null;
	}

	public void updateUser(User user) {
		try (Connection connection = JDBCUtils.getConnection();

			 PreparedStatement preparedStatement = connection.prepareStatement("update users set first_name = ?, last_name = ? where username = ?")) {
			preparedStatement.setString(1, user.getFirstName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setString(3, user.getUsername());

			preparedStatement.execute();
		} catch (SQLException e) {
			JDBCUtils.printSQLException(e);
		}
	}
}
