<%@ page language="java"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">
	<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_tipolistado" ignore="true" /></title>	
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v1/js/globales.js"></script>
	
	<jsp:include page="/v1/general/scriptmenu.jsp"/>
	

	</head>

	
	<body>
		
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v1/general/cabecera.jsp"/>

			<!-- continguts -->
			<div id="continguts">
				
				<jsp:include page="/v1/general/inc_llistats_campana_menu.jsp"/>
		
											 
				<jsp:include page="/general/inc_llistats_div_info.jsp"/>
				
				<jsp:include page="/general/inc_llistats_capcelera_buscador.jsp"/>
									
									<logic:notEqual name="MVS_parametros_pagina" property="nreg" value="0">  

												<jsp:include page="/general/inc_llistats_info_cursor.jsp"/>
											
												<div id="noticiesLlistat">
												<ul>
												<logic:iterate name="MVS_listado" id="i">
												<logic:notEmpty name="i" property="traduce.titulo">
													<li style="list-style: none;">
														<img src="imgs/listados/bullet_gris.gif" alt="" />
														<a href="<bean:write name="i" property="traduce.laurl" />" target="_blank">
														<bean:write name="i" property="traduce.titulo" ignore="true"/>
														</a>
														<logic:notEmpty name="i" property="traduce.texto" >
														<bean:write name="i" property="traduce.texto"  filter="false"/>
														</logic:notEmpty>
														<a href="<bean:write name="i" property="traduce.laurl" />" target="_blank"><bean:write name="i" property="traduce.laurl" ignore="true"/></a>
													</li>
												</logic:notEmpty>
												</logic:iterate>
												</ul>
												</div>
												
												<jsp:include page="/general/inc_llistats_paginacio.jsp"/>
												
									</logic:notEqual>						
				</div>	
				<!-- fin informacio -->
				

			</div>
			<!-- fin continguts -->
			
			
			<!-- peu -->
			<jsp:include page="/v1/general/pie.jsp"/>
			<!-- fin peu -->
		</div>	
	</body>
</html>


