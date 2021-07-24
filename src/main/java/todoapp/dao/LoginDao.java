package todoapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import todoapp.model.LoginBean;
import todoapp.model.User;
import todoapp.utils.JDBCUtils;

public class LoginDao {
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User validate(LoginBean loginBean) throws ClassNotFoundException {
		boolean status = false;

		Class.forName("com.mysql.jdbc.Driver");

		User user;
//делается это так то он берет у JDBC
		try (Connection connection = JDBCUtils.getConnection();//делает подклюение у JDBCUtils

			 PreparedStatement preparedStatement = connection//делает prepareStatement подставляет логин и пароль
					 .prepareStatement("select * from users where username = ?")) {
			preparedStatement.setString(1, loginBean.getUsername());// первое поиия и откуда берет

			ResultSet rs = preparedStatement.executeQuery();//выполняет стайтмент
			if (!rs.next()) {
				user = null;
			} else {
				user = new User(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				if (!passwordEncoder.matches(loginBean.getPassword(), user.getPassword())) {
					user = null;
				}
			}
		} catch (SQLException e) {
			JDBCUtils.printSQLException(e);
			throw new IllegalArgumentException(e);
		}
		return user;
	}
}