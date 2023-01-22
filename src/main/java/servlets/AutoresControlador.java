package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.GestorBD;


@WebServlet(name = "AutoresControlador", urlPatterns = {"/AutoresControlador"})
public class AutoresControlador extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GestorBD bd= new GestorBD();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("autorCompleto", bd.autorCompleto());
		
		response.sendRedirect("autores.jsp");
	}

}
