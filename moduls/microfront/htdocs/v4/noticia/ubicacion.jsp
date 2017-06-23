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
	<logic:present name="MVS_noticia" property="traduce">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_noticia" property="traduce.titulo" filter="false"/></title>
	</logic:present>
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
				<!-- campanya -->
				<logic:present name="MVS_home_tmp_campanya">
					<bean:write name="MVS_home_tmp_campanya" filter="false"/>
				</logic:present>
				<!-- fin campanya -->				
				<!-- menu -->
				<logic:present name="MVS_microsite">
							<logic:equal name="MVS_microsite" property="tipomenu" value="1">
								<div id="marcLateral">
									<jsp:include page="/v4/general/menu.jsp"/>
								</div>					
							</logic:equal>
							<logic:equal name="MVS_microsite" property="tipomenu" value="2">
								<div id="marcLateralAmbIcones">
									<jsp:include page="/v4/general/menu.jsp"/>
								</div>					
							</logic:equal>
				</logic:present>	
				<!-- fin menu -->
											 
				<!-- informacio con o sin menu -->
			<logic:equal name="MVS_microsite" property="tipomenu" value="0">
				<div id="infoNoMenu">
			</logic:equal>
			<logic:notEqual name="MVS_microsite" property="tipomenu" value="0">
				<div id="info">
			</logic:notEqual>								
					<!-- titol -->
					
			<logic:present name="MVS_forzarmapa">	
				<logic:equal name="MVS_forzarmapa" value="true">					
					<h2 ><bean:message key="noticia.ubicacion"/> <bean:write name="MVS_tiponoticia" /></h2>								
				</logic:equal>
				<logic:notEqual name="MVS_forzarmapa" value="true">					
					<h2 ><bean:message key="noticia.detalle"/> <bean:write name="MVS_tiponoticia" /></h2>							
				</logic:notEqual>	
			</logic:present>
			<logic:notPresent name="MVS_forzarmapa">																		
				<h2 ><bean:message key="noticia.detalle"/> <bean:write name="MVS_tiponoticia" /></h2>																	
			</logic:notPresent>		
					
					
					
					
					<!-- Mapa -->
					<div id="gusiteMaps"></div>																	
					<logic:present name="MVS_noticia" property="traduce">
						<div style="display: none;" class="gusiteMapsMarker">
							<input class="gMMLatitud"  value="<bean:write name="MVS_noticia" property="latitud" />" />
							<input class="gMMLongitud"  value="<bean:write name="MVS_noticia" property="longitud" />" />
							<input class="gMMColor"  value="<bean:write name="MVS_noticia" property="colorIcono" />" />
							<input class="gMMTitulo"  value="<bean:write name="MVS_noticia" property="traduce.titulo" />" />
							<div class="gMMContenido">
								<div class="gMMInfoWindow">
									<h3><bean:write name="MVS_noticia" property="traduce.titulo" ignore="true" filter="false"/></h3>									
									<logic:notEmpty name="MVS_noticia" property="traduce.subtitulo">
									<h4>
										<bean:write name="MVS_noticia" property="traduce.subtitulo" ignore="true" filter="false"/>
									</h4>
									</logic:notEmpty>
									<logic:notEmpty name="MVS_noticia" property="imagen">
										<img src="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="MVS_noticia" property="id" />ZI<bean:write name="MVS_noticia" property="imagen.id" />&amp;id=<bean:write name="MVS_noticia" property="imagen.id" />" alt="<bean:write name="MVS_noticia" property="traduce.titulo" ignore="true" filter="false"/>"  align="middle"/>
									</logic:notEmpty>
									<logic:notEqual name="MVS_forzarmapa" value="true">
										<logic:notEmpty name="MVS_noticia" property="traduce.texto">	
											<p>									
												<bean:write name="MVS_noticia" property="traduce.texto" ignore="true" filter="false"/>
											</p>											
										</logic:notEmpty>
										<logic:notEmpty name="MVS_noticia" property="traduce.docu">
											<p><bean:message key="noticia.descdocumento"/></p>
											<p><bean:message key="general.archivo"/> <bean:write name="MVS_noticia" property="traduce.docu.mime" />, <bean:write name="MVS_noticia" property="traduce.docu.peso" /> bytes - 
											<a href="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="MVS_noticia" property="id" />ZI<bean:write name="MVS_noticia" property="traduce.docu.id" />&amp;id=<bean:write name="MVS_noticia" property="traduce.docu.id" />" target="blank"><bean:write name="MVS_noticia" property="traduce.docu.nombre" /></a></p>
										</logic:notEmpty>
									</logic:notEqual>
								</div>
							</div>
						</div>								
					</logic:present>
					
					<script type="text/javascript" src="v4/js/gusiteMaps.js"></script>														
					<script src="https://maps.googleapis.com/maps/api/js?key=<%=GusitePropertiesUtil.getKeyGooglemaps() %>&amp;callback=initialize" async="async" defer="defer" ></script>
	<logic:present name="MVS_forzarmapa">	
		<logic:equal name="MVS_forzarmapa" value="true">
			<logic:present name="MVS_menu_cont_notic">								
				<logic:equal name="MVS_menu_cont_notic" value="-1">
					<div class="gMMBVolver" ><a class="gMMBVolver" href="noticia.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="MVS_noticia" property="id" />&amp;lang=<bean:write name="MVS_idioma" />" ><bean:message key="noticia.volver"/></a></div>							
				</logic:equal>
				<logic:notEqual name="MVS_menu_cont_notic" value="-1">
					<div class="gMMBVolver" ><a class="gMMBVolver" href="noticia.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="MVS_noticia" property="id" />&amp;lang=<bean:write name="MVS_idioma" />&amp;mcont=<bean:write name="MVS_menu_cont_notic" />"><bean:message key="noticia.volver"/></a></div>							
				</logic:notEqual>
			</logic:present>
			<logic:notPresent name="MVS_menu_cont_notic">																		
				<div class="gMMBVolver" ><a class="gMMBVolver" href="noticia.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="MVS_noticia" property="id" />&amp;lang=<bean:write name="MVS_idioma" />"><bean:message key="noticia.volver"/></a></div>																	
			</logic:notPresent>								
		</logic:equal>	
	</logic:present>	
								
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
