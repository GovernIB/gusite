<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>  Gestor Microsites</title>
	
	<!-- estils -->
	<link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
	<!-- /estils -->
	
	<!-- js -->
	 <script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript" src="js/rArxius.js"></script>
	<!-- /js -->
	
	<!--[if lt IE 7]>
		<link rel="stylesheet" type="text/css" href="css/estils-v.4.1_ie6.css" media="screen" />
	<![endif]-->
	
</head>
	<body>

<!-- contenidor -->
	<div id="contenidor">
	
	 
		
	<!-- continguts -->
	<div id="continguts">
		<!-- titol pagina -->
		<h1><img src="imgs/titulos/contenido.gif" alt="Llistats" />  Errors Accessibilitat  </h1>
		</br> </br> 
		
	<logic:notEmpty name="lista">	
	 <table id="llistat">
	 	 
	  <bean:define id="itemantes">0</bean:define>
	  <logic:iterate id="i" name="lista" indexId="indice">
		  <bean:define id="item"><bean:write name="i" property="iditem" /></bean:define>
		   <logic:notEqual  name="i" property="iditem" value="<%=itemantes%>">
		  	 <thead>
					<tr>
						<th></b>
						  <logic:equal  name="tipo" value="c">
						  	<a href="contenidoEdita.do?id=<bean:write name="i" property="iditem" />">Tornar Contigut</a> </th>
						  </logic:equal>
						  <logic:equal  name="tipo" value="a">
						  	<a href="agendaEdita.do?id=<bean:write name="i" property="iditem" />">Tornar Agenda</a> </th>
						  </logic:equal>
						  <logic:equal  name="tipo" value="n">
						  	<a href="noticiaEdita.do?id=<bean:write name="i" property="iditem" />">Tornar Noticia</a> </th>
						  </logic:equal>
					</tr>
	 		</thead>
				 
			 
	 	   <bean:define id="itemantes"><bean:write name="i" property="iditem" /></bean:define>
	     	   </logic:notEqual>
	     	   <tbody>
	     	   	 <tr>
				<td> <bean:write name="i" property="idioma" /> -> <bean:write name="i" property="mensaje" />  </td>
			</tr>
	  </logic:iterate>
			 
			</tbody>
		</table>
	</logic:notEmpty>
	
	<logic:empty name="lista">	
			
			<bean:message key="mensa.Accessibilitatok"/>
	</logic:empty>  
	</div>
		 
</body>
</html>
 
	 <jsp:include page="/moduls/pieControl.jsp"/>

	 
 
 