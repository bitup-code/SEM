package todoapp.web;

import todoapp.dao.UserDao;
import todoapp.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
    UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getAttribute("user");
        if (user == null) {//если юзер null отправляем на логин если нет то на профиль
            response.sendRedirect("login/login.jsp");
            return;
        }
        response.sendRedirect("profile/profile.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");//правим кодировку
        User user = (User) request.getAttribute("user");//пытаемся достать юзера из отребудтов
        if (user == null) {//если юзер null перебрасываем на логин
            response.sendRedirect("login/login.jsp");
            return;
        }
        String value = request.getParameter("firstName");//если залогинен
        if (value != null && value.length() > 0) user.setFirstName(value);//если длинна и вэлью не ноль то мы меняем
        value = request.getParameter("lastName");//можно меня только отдельные поля и никаких конфликтов не будет
        if (value != null && value.length() > 0) user.setLastName(value);
        userDao.updateUser(user);//обновляем базу
        response.sendRedirect("profile/profile.jsp");
    }
}
