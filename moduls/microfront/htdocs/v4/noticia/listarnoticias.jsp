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
	<script type="text/javascript" src="v4/js/globales.js"></script>
	
	<jsp:include page="/v4/general/scriptmenu.jsp"/>
	

	</head>

	
	<body>
		
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v4/general/cabecera.jsp"/>

			<!-- continguts -->
			<div id="continguts">

				
				<jsp:include page="/v4/general/inc_llistats_campana_menu.jsp"/>
											 
				<jsp:include page="/general/inc_llistats_div_info.jsp"/>
				
				<jsp:include page="/general/inc_llistats_capcelera_buscador.jsp"/>

									
									<logic:notEqual name="MVS_parametros_pagina" property="nreg" value="0">
									
											<jsp:include page="/general/inc_llistats_info_cursor.jsp"/>
									  
											<div id="noticiesLlistat">
												<ul style= "text-align:justify">
												<logic:iterate name="MVS_listado" id="i">
												<logic:notEmpty name="i" property="traduce.titulo">
													<li style="list-style:none;">										
													<logic:notEmpty name="i" property="imagen">
														<img src="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="i" property="id" />ZI<bean:write name="i" property="imagen.id" />&amp;id=<bean:write name="i" property="imagen.id" />" alt="" width="48" height="48" align="middle"/>
													</logic:notEmpty>
													<logic:empty name="i" property="imagen">
														<img src="imgs/noticies/news.gif" alt="" align="middle"/>
													</logic:empty>
														<bean:write name="i" property="fpublicacion" format="dd/MM/yyyy" /><logic:notEmpty name="i" property="traduce.fuente"> </logic:notEmpty>
														<span class="enllas">
														
														<bean:define id="idcontenido" value="-1" type="java.lang.Object"/>
														<logic:present name="MVS_menu_cont_notic">
															<bean:define id="idcontenido" name="MVS_menu_cont_notic" type="java.lang.String"/>
														</logic:present>
														<% if (!idcontenido.equals("-1")){ 
														%>
															<a href="noticia.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="i" property="id" />&amp;lang=<bean:write name="MVS_idioma" />&amp;mcont=<bean:write name="MVS_menu_cont_notic" />"><bean:write name="i" property="traduce.titulo" ignore="true" filter="false"/></a>
														<%
														}else{ 
														%>
															<a href="noticia.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="i" property="id" />&amp;lang=<bean:write name="MVS_idioma" />"><bean:write name="i" property="traduce.titulo" ignore="true" filter="false"/></a>
														<%
														} 
														%>
														<logic:notEmpty name="i" property="traduce.subtitulo">
														<span class="enllas">
															[<bean:write name="i" property="traduce.subtitulo" ignore="true" filter="false"/>]
														</span>
														</logic:notEmpty>
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
			<jsp:include page="/v4/general/pie.jsp"/>
			<!-- fin peu -->
		</div>	
	</body>
</html>


