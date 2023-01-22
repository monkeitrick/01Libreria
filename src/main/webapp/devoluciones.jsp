<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${librosPrestados==null}">
	<jsp:forward page="ServletDevolver"/>
</c:if>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Insert title here</title>
	</head>
	<body>
		<ol>
			<c:forEach items="${librosPrestados.keySet()}" var="libro">
				<li>
					${libro}, ${librosPrestados.get(libro)} Dias Prestado, 
					<c:choose>
						<c:when test="${devoluciones == null}">
						<a href="ServletDevolver?libroDevuelto=${libro}">Marcar Devolucion</a>
						
						</c:when>
						<c:otherwise>
								<c:choose>
									<c:when test="${devoluciones.contains(libro)}">
										<a href="ServletDevolver?libroReservado=${libro}">Revertir Devolucion</a>
										
									</c:when>
									<c:otherwise>
										<a href="ServletDevolver?libroDevuelto=${libro}">Marcar Devolucion</a>
										
									</c:otherwise>
									</c:choose>
						</c:otherwise>
					</c:choose>
				</li>
			</c:forEach>
		</ol>
		<c:if test="${devoluciones!=null}">
			<p><a href="ServletDevolver?borrarLibros=si">Grabar Devoluciones</a></p>
		</c:if>
	</body>
</html>