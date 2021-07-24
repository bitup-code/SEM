package todoapp.utils;

import todoapp.dao.UserDao;
import todoapp.model.User;

import javax.servlet.http.Cookie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CookieUtil {

    public Cookie assignCookie(String username) {
        Cookie cookie;
        try (Connection connection = JDBCUtils.getConnection();//делает подклюение у JDBCUtils
             PreparedStatement preparedStatement = connection
                     .prepareStatement("insert into cookie values (?, (select id from users where username = ?))")) {
            String uuid = UUID.randomUUID().toString();//вставляет в базу uid и ассоциированное с ним id юзера
            preparedStatement.setString(1, uuid);//но так как мы не знаем id делаем подзапрос и по имени мы достаем id
            preparedStatement.setString(2, username);
            preparedStatement.execute();
            cookie = new Cookie("auth", uuid);//создаем новую куку с именем auth и value ее id
            cookie.setMaxAge(1000000);//"срок жизни куки " вроде пару месяцев
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
            throw new IllegalArgumentException(e);
        }
        return cookie;//отдаем эту кукуи браузер ее запомнит
    }

    public User findUser(String uuid) {
        try (Connection connection = JDBCUtils.getConnection();//делает подклюение у JDBCUtils
             PreparedStatement preparedStatement = connection//запрос который достается из базы данных
                     .prepareStatement("select first_name, last_name, username from cookie join users u on uuid = ? and u.id = cookie.id")) {
            //стандартный запрос тех полей которые нам нужны и через куки достае все что связывает его с этим пользователем
            preparedStatement.setString(1, uuid);

            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            User user = new User(rs.getString(1), rs.getString(2), rs.getString(3));//тут все собирается и возвращает юзера
            return user;
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
            throw new IllegalArgumentException(e);
        }
    }

    public void deleteCookie(String uuid) {
        try (Connection connection = JDBCUtils.getConnection();//делает подклюение у JDBCUtils
             PreparedStatement preparedStatement = connection
                     .prepareStatement("delete from cookie where uuid = ?")) {
            preparedStatement.setString(1, uuid);

            preparedStatement.execute();
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
            throw new IllegalArgumentException(e);
        }
    }
}
