package todoapp.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import todoapp.model.LoginBean;
import todoapp.dao.LoginDao;
import todoapp.model.User;
import todoapp.utils.CookieUtil;


@WebServlet("-")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginDao loginDao;
	private CookieUtil cookieUtil;

	public void init() {
		loginDao = new LoginDao();
		cookieUtil = new CookieUtil();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getAttribute("user") != null) {//смотрим есть ли этот юзер который мы положили в запрос если есть то
			//пользователь уже авторизован и отправляем на профиль
			response.sendRedirect("profile/profile.jsp");//загружаем profile.jsp
		} else {//если нет то  на логин
			response.sendRedirect("login/login.jsp");//загружаем login.jsp
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		authenticate(request, response);//перенаправляет на authenticate
	}

	private void authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String username = request.getParameter("username");//достаем из формы имя пороль и запомнить и складываем в LoginBean
		String password = request.getParameter("password");
		String remember = request.getParameter("remember");
		LoginBean loginBean = new LoginBean();//берет данные и грузим в форму
		loginBean.setUsername(username);
		loginBean.setPassword(password);//берет логин и пароль и загружает их в loginBean

		try {
			User user;
			if ((user = loginDao.validate(loginBean)) != null) {//говорим loginDao что б он проверил есть ли то это существует и вернул юзера
				request.getSession().setAttribute("user", user);//добавляем в сессию этого юзера
				if ("on".equals(remember)) {//если отмечаем галку запомнить меня то добавляем к запросу куку
					response.addCookie(cookieUtil.assignCookie(username));
				}
				response.sendRedirect("/list");
			} else {
				HttpSession session = request.getSession();//ну а тут я не дописал но в общем если о апись не нашел он ничего не делает

			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
