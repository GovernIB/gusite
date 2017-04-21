<%@page import="es.caib.gusite.utilities.property.GusitePropertiesUtil"%>
<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.png" type="image/x-ico"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">
		<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_tipolistado" ignore="true" /></title>			
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v4/js/jquery/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="v4/js/globales.js"></script>

	<jsp:include page="/v4/general/scriptmenu.jsp"/>
	

	</head>

	
	<body>
		
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v4/general/cabecera.jsp"/>

			<!-- continguts -->
			<div id="continguts">

				<!-- lu lc -->
				<jsp:include page="/v4/general/inc_llistats_campana_menu.jsp"/>
							<!-- lu li  -->				 
				<jsp:include page="/general/inc_llistats_div_info.jsp"/>
				<!-- lu capbusca -->
				<jsp:include page="/general/inc_llistats_capcelera_buscador.jsp"/>

								<!-- lu parampag  -->	
					<logic:notEqual name="MVS_parametros_pagina" property="nreg" value="0">
					<!-- lu info cursor -->
							<jsp:include page="/general/inc_llistats_info_cursor.jsp"/>
					  <!-- lu noti llistat -->
							<div id="noticiesLlistat">							
								<div id="gusiteMaps"></div>	
							<logic:iterate name="MVS_listado" id="i">
								<logic:notEmpty name="i" property="traduce.titulo">
									<div style="display: none;" class="gusiteMapsMarker">
										<input class="gMMLatitud"  value="<bean:write name="i" property="latitud" />" />
										<input class="gMMLongitud"  value="<bean:write name="i" property="longitud" />" />
										<input class="gMMColor"  value="<bean:write name="i" property="colorIcono" />" />
										<input class="gMMTitulo"  value="<bean:write name="i" property="traduce.titulo" />" />
										<div class="gMMContenido">
											<div class="gMMInfoWindow">
												<h3><bean:write name="i" property="traduce.titulo" ignore="true" filter="false"/></h3>
												
												<logic:notEmpty name="i" property="traduce.subtitulo">
												<h4>
													<bean:write name="i" property="traduce.subtitulo" ignore="true" filter="false"/>
												</h4>
												</logic:notEmpty>
												<logic:notEmpty name="i" property="imagen">
													<img src="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="i" property="id" />ZI<bean:write name="i" property="imagen.id" />&amp;id=<bean:write name="i" property="imagen.id" />" alt="<bean:write name="i" property="traduce.titulo" ignore="true" filter="false"/>"  align="middle"/>
												</logic:notEmpty>
												<logic:notEmpty name="i" property="traduce.texto">	
													<p>									
														<bean:write name="i" property="traduce.texto" ignore="true" filter="false"/>
													</p>											
												</logic:notEmpty>
												<logic:notEmpty name="i" property="traduce.docu">
													<p><bean:message key="noticia.descdocumento"/></p>
													<p><bean:message key="general.archivo"/> <bean:write name="i" property="traduce.docu.mime" />, <bean:write name="i" property="traduce.docu.peso" /> bytes - 
													<a href="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="i" property="id" />ZI<bean:write name="i" property="traduce.docu.id" />&amp;id=<bean:write name="i" property="traduce.docu.id" />" target="blank"><bean:write name="i" property="traduce.docu.nombre" /></a></p>
												</logic:notEmpty>
											</div>
										</div>
									</div>
									
								</logic:notEmpty>
								</logic:iterate>
								<script type="text/javascript" src="v4/js/gusiteMaps.js"></script>														
							 	<script src="https://maps.googleapis.com/maps/api/js?key=<%=GusitePropertiesUtil.getKeyGooglemaps() %>&amp;callback=initialize" async="async" defer="defer" ></script>
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


