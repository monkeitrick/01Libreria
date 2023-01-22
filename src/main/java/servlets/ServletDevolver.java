package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.GestorBD;

@WebServlet(name = "ServletDevolver", urlPatterns = {"/ServletDevolver"})
public class ServletDevolver extends HttpServlet {
	private ArrayList<String> devoluciones= new ArrayList<String>();
	private static final long serialVersionUID = 1L;
	private GestorBD bd= new GestorBD();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("librosPrestados")==null) {
			request.getSession().setAttribute("librosPrestados",bd.librosPrestados());
		}
		if(request.getParameter("libroDevuelto")!=null) {
			if(request.getSession().getAttribute("devoluciones")!=null) {
				devoluciones=(ArrayList<String>) request.getSession().getAttribute("devoluciones");
			}
			devoluciones.add(request.getParameter("libroDevuelto"));
			request.getSession().setAttribute("devoluciones", devoluciones);
//			System.out.println(request.getParameter("libroDevuelto"));
//			System.out.println("entra");
		}
		else {
			if(request.getParameter("libroReservado")!=null) {
				devoluciones=(ArrayList<String>) request.getSession().getAttribute("devoluciones");
				devoluciones.remove(request.getParameter("libroReservado"));
				request.getSession().setAttribute("devoluciones", devoluciones);
			}
		}
		if(request.getParameter("borrarLibros")!=null) {
			ArrayList<String> devoluciones= (ArrayList<String>) request.getSession().getAttribute("devoluciones");
			for(int i=0;i<devoluciones.size();i++) {
				bd.borrarLibros(devoluciones.get(i));
			}
			request.getSession().setAttribute("librosPrestados",bd.librosPrestados());
		}
		response.sendRedirect("devoluciones.jsp");
	}

}
