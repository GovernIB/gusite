<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
	
		<!-- cap -->
		<jsp:include page="cabecera.jsp"/>
		<!-- /cap -->

		<!-- marc lateral -->
		<jsp:include page="menuLateralIzq.jsp"/>
		<!-- /marc lateral -->
		
		<!-- continguts -->
		<div id="continguts">
		<!-- titol pagina -->
		<h1><img src="imgs/titulos/contenido.gif" alt="Llistats" /><bean:message key="log.consola" /></h1>
			 
	<p>
	<bean:message key="log.consola.desc1" /> 
	<bean:message key="log.consola.desc2" />
	</p>

	<p>
	<logic:present name="logs">
		<!-- tabla listado -->
		<table id="llistat">
			<thead>
				<tr>
					<th><bean:message key="log.consola.cap" /></th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td><bean:message key="log.consola.peu" /></td>
				</tr>
			</tfoot>								
			<tbody>
				<logic:iterate name="logs" id="i">
					<tr>
						<td><bean:write name="i" filter="false" /></td>
					</tr>
				</logic:iterate>
			</tbody>
		</table>
		<!-- /tabla listado -->
	</logic:present>
	</p>
</div>
		<!-- /continguts -->
		<!-- peu -->
		<jsp:include page="peu.jsp"/>
		<!-- /peu -->			
	</div>
	<!-- contenidor -->
</body>
</html>
