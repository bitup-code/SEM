package todoapp.web;

import todoapp.model.User;
import todoapp.utils.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    CookieUtil cookieUtil;

    @Override
    public void init() throws ServletException {
        cookieUtil = new CookieUtil();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getAttribute("user");
        if (user != null) {//если user не null
            request.getSession().invalidate();//достаем сессию и удаляем все данные из сессии
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {//проходимся по кукам и если они не null
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("auth")) {//ищем куку с именем auth если мы ее находим
                        cookie.setMaxAge(0);//ставим 0
                        cookieUtil.deleteCookie(cookie.getValue());//обращаемся к кукиутил что бы он удалил эту куку из базы данных
                        response.addCookie(cookie);//и прекрепляем эту куку к запросу и когда браузер ее получит он чистит куки
                        break;
                    }
                }
            }
        }
        response.sendRedirect("/login");
    }
}
