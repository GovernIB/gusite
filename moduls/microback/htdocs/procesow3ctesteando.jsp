<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	
	
</head>

<body>	
	<!-- molla pa -->
	<ul id="mollapa">
			<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
			<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
			<li><bean:message key="menu.ferramentes" /></li>
			<li><a href="procesow3c.do?idsite=<bean:write name="MVS_microsite" property="id"/>">Test Accessibilitat W3C</a></li>
			<li class="pagActual">Testeando....</li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/taww3c.gif" alt="Accesibilidad" />Test Accessibilitat. <span>Testeando....</span></h1>
	<!-- continguts -->
	<br/>
	
		<logic:present name="testeando">
		
		<iframe src="procesow3ctesteandopintastatus.do?idstatusbar=<bean:write name="MVS_idbarraestado"/>" id="testeandow3cIF" name="testeandow3cIF" frameborder="0" scrolling="no" width="400" height="65"></iframe>
						
						
			<p>
			<strong>
				&nbsp;Aquest procés pot trigar diversos minuts depenent  de la grandaria del microsite.<br/> 
				&nbsp;Encara que canvie de pantalla, el procés continuarà .<br/>
			</strong>
			</p>
			
		</logic:present>
		

</body>
</html>


<jsp:include page="/moduls/pieControl.jsp"/>
