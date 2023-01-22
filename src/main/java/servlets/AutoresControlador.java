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
		if(request.getParameter("id")!=null) {
			String autor=request.getParameter("id");
			request.getSession().setAttribute("librosAutor", bd.librosAutor(autor));
			request.getSession().setAttribute("autorLibros", bd.fromIdToName(autor));
		}
		if(request.getParameter("idPrestamo")!=null) {
			String id=request.getParameter("idPrestamo");
			bd.reservarLibro(id);
		}
		if(request.getParameter("nombre")!=null && !request.getParameter("nombre").equals("")) {
			String nombre=request.getParameter("nombre");
			if(request.getParameter("fecha")!=null && !request.getParameter("fecha").equals("")) {
				String fecha=request.getParameter("fecha");
				if(request.getParameter("nacionalidad")!=null && !request.getParameter("nacionalidad").equals("")) {
					String nacionalidad=request.getParameter("nacionalidad");
					String existe=bd.autorExistente(nombre);
					if(existe.equals("true")) {
						request.getSession().setAttribute("error", "El autor"+nombre+"ya existe");
					}
					else {
						bd.aniadirAutor(nombre, fecha, nacionalidad);
						System.out.println("entra");
						request.getSession().setAttribute("metido", "Autor introducido correctamente");
					}
				}else {
					request.getSession().setAttribute("error", "Hay que rellenar todos los datos");
				}
			}else {
				request.getSession().setAttribute("error", "Hay que rellenar todos los datos");
			}
		}
		else {
			request.getSession().setAttribute("error", "Hay que rellenar todos los datos");
		}
		response.sendRedirect("autores.jsp");
	}

}
