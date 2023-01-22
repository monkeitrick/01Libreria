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
					<td><a href="autores.jsp?id=${autor.getIdAutor()}">Ver Libros</a></td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>