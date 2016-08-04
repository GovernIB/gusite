<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ page language="java"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="es">
	<head>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.png" type="image/x-ico"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Página para testear accesibilidad con taw</title>
	<link href="v4/css/estils.css" rel="stylesheet" type="text/css" />
	</head>

	
	<body>

			<logic:present name="MVS_noticia">
				<!-- titol -->
				<h1 ><bean:write name="MVS_noticia" property="traduce.titulo" filter="false"/></h1>
				<h2><bean:write name="MVS_noticia" property="traduce.subtitulo" filter="false"/></h2>
				<p><bean:write name="MVS_noticia" property="traduce.texto" filter="false"/></p>
			</logic:present>
			
			<logic:present name="MVS_agenda">
				<h1 ><bean:write name="MVS_agenda" property="traduce.titulo" filter="false"/></h1>
				<p><bean:write name="MVS_agenda" property="traduce.descripcion" filter="false"/></p>
			</logic:present>
			
			<logic:present name="MVS_contenido">
				<h1 ><bean:write name="MVS_contenido" property="traduce.titulo" filter="false"/></h1>
				<bean:write name="MVS_contenido" property="traduce.texto" filter="false"/>
			</logic:present>
			
	</body>
</html>
