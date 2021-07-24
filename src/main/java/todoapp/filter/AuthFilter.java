package todoapp.filter;

import todoapp.dao.UserDao;
import todoapp.model.User;
import todoapp.utils.CookieUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/*") //тк он замапен на "/*" то любой запрос проходит ерез филтр
public class AuthFilter implements Filter {
    UserDao userDao;
    CookieUtil cookieUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userDao = new UserDao();
        cookieUtil = new CookieUtil();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = ((HttpServletRequest)request);
        HttpSession session = servletRequest.getSession(false);//  не создаем новую ссесию если ее нет
        if (session != null) {//если сессия не null то
            //сессия у пользователя эта кука с определенным айди у нас это обьект который можно найти по этому айди
            //и положить туда что захотим например пользователя собственно если пользователь авторизован то у него должен быть этот атрибут
            User user = (User) session.getAttribute("user");// смотрим есть ли в ней пользователь
            if (user != null) {//если он там есть то мы добавляем его в запрос
                request.setAttribute("user", user);
            }
        } else {//если сессия null то  наинаем смотреть по куки
            Cookie[] cookies = servletRequest.getCookies();
            if (cookies != null) {//если куки не null то ищем куку с именем auth
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("auth")) {
                        User user = cookieUtil.findUser(cookie.getValue());// достаем из нее куки Value+
                        request.setAttribute("user", user);
                        servletRequest.getSession().setAttribute("user", user);//добавляем в рк запрос то же самое что мы делали выше
                        break;
                    }
                }
            }
        }

        chain.doFilter(request, response);//передает chain.doFilter следующему по порядку фильтру он у нас один то он отправляет
        //на один из контроллеров
    }

    @Override
    public void destroy() {

    }
}
