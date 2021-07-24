package todoapp.web;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import todoapp.model.Todo;
import todoapp.dao.TodoDao;
import todoapp.dao.TodoDaoImpl;
import todoapp.model.User;


@WebServlet("/")
public class TodoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TodoDao todoDAO;

	public void init() {
		todoDAO = new TodoDaoImpl();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)//при попытке  doPost перенапрявляет на doGet
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user;
		if ((user = (User) request.getAttribute("user")) == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("login/login.jsp");
			dispatcher.forward(request, response);
			return;
		}
		String action = request.getServletPath();//в doGet мы выываем action

		try {//выбираем меду несколькими лементами
			switch (action) {
				case "/new":
					showNewForm(request, response);
					break;
				case "/insert":
					insertTodo(request, response);
					break;
				case "/delete":
					deleteTodo(request, response);
					break;
				case "/edit":
					showEditForm(request, response);
					break;
				case "/update":
					updateTodo(request, response);
					break;
				case "/list":
					listTodo(request, response);
					break;
				default: {
					RequestDispatcher dispatcher = request.getRequestDispatcher("profile/profile.jsp");
					dispatcher.forward(request, response);
					break;
				}
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listTodo(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Todo> listTodo = todoDAO.selectAllTodos();//Listtodo обращается в tododao достать все ту ду
		request.setAttribute("listTodo", listTodo);//в реквест добавляет атрибут тудулист
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todo-list.jsp");//чтоб его было видно в диспатчере
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todo-form.jsp");//открывают страницу шобы сделать новую ту ду
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));//достает айди
		Todo existingTodo = todoDAO.selectTodo(id);//туду с тим айдишником
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todo-form.jsp");//отдает диспатчеру
		request.setAttribute("todo", existingTodo);
		dispatcher.forward(request, response);

	}

	private void insertTodo(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

		String title = request.getParameter("title");
		String username = request.getParameter("username");
		String description = request.getParameter("description");//достает юердискрип



		boolean isDone = Boolean.valueOf(request.getParameter("isDone"));
		Todo newTodo = new Todo(title, username, description, LocalDate.now(), isDone);//достает новое туду
		todoDAO.insertTodo(newTodo);
		response.sendRedirect("list");
	}

	private void updateTodo(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));

		String title = request.getParameter("title");
		String username = request.getParameter("username");
		String description = request.getParameter("description");

		LocalDate targetDate = LocalDate.parse(request.getParameter("targetDate"));

		boolean isDone = Boolean.valueOf(request.getParameter("isDone"));
		Todo updateTodo = new Todo(id, title, username, description, targetDate, isDone);//тое содает новое туду

		todoDAO.updateTodo(updateTodo);//только выывает метод апдейт

		response.sendRedirect("list");
	}

	private void deleteTodo(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));//берет айди
		todoDAO.deleteTodo(id);//удаляет по айдишнику
		response.sendRedirect("list");
	}
}
