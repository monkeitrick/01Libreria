<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${autorCompleto==null}">
	<jsp:forward page="AutoresControlador"/>
</c:if>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Insert title here</title>
	</head>
	<body>
		<h1>Lista de Autores</h1>
		<table>
			<tr>
				<th>Nombre</th>
				<th>Fecha de nacimiento</th>
				<th>Nacionalidad</th>
				<th>Ver Libros</th>
			</tr>
			<c:forEach items="${autorCompleto}" var="autor">
				<tr>
					<td>${autor.getNombre()}</td>
					<td><fmt:formatDate value="${autor.fechanac}" pattern="yyyy/MM/dd"/></td>
					<td>${autor.getFechanac()}</td>
					<td><a href="AutoresControlador?id=${autor.getIdAutor()}">Ver Libros</a></td>
				</tr>
			</c:forEach>
		</table>
		<h1>Añadir Autor</h1>
		<form action="" method="post">
		<table>
			<tr>
				<td><label for="nombre">Nombre:</label></td>
				<td><input type="text" name="nombre" id="nombre"></td>
			</tr>
			<tr>
				<td><label for="nombre">Fecha de Nacimiento(XXXX-XX-XX):</label></td>
				<td><input type="text" name="fecha" id="fecha"></td>
			</tr>
			<tr>
				<td><label for="nombre">Nacionalidad:</label></td>
				<td><input type="text" name="nacionalidad" id="nacionalidad"></td>
			</tr>
			<tr><td><input type="submit" value="Añadir"></td></tr>
		</table>
        </form>
        <c:if test="${error!=null}">
        	<p>${error}</p>
        </c:if>
        <c:if test="${metido!=null}">
        	<p>${metido}</p>
        </c:if>
        <c:if test="${librosAutor!=null}">
        	<h1>Libros de ${autorLibros}</h1>
	        <ul>
	        	<c:forEach items="${librosAutor}" var="libro">
					<tr>
						<li><a href="AutoresControlador?idPrestamo=${libro.getIdLibro()}">${libro.getTitulo()}</a></li>
					</tr>
				</c:forEach>
			</ul>
		</c:if>
	</body>
</html>