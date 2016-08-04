<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">	
	<logic:present name="MVS_encuesta">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_encuesta" property="traduce.titulo" ignore="true" /> - <bean:message key="encuesta.resultados"/></title>
	</logic:present>
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
				<!-- trazabilidad -->
				<ul id="mollaPa">
					<li><a href="home.do?idsite=<bean:write name="MVS_idsite" />&amp;lang=<bean:write name="MVS_idioma" />"><bean:message key="general.inicio"/></a></li>
					<li><bean:write name="MVS_encuesta" property="traduce.titulo" ignore="true" />: <bean:message key="encuesta.resultados"/></li>
				</ul>
				
				
				<!-- campanya -->
				<logic:present name="MVS_home_tmp_campanya">
					<bean:write name="MVS_home_tmp_campanya" filter="false"/>
					<!-- Capa separadora. NO quitar nunca  -->	
					<div id="enllasDestPeu"></div>
				</logic:present>
				<!-- fin campanya -->				


				<!-- menu -->
					<logic:present name="MVS_microsite">
							<logic:equal name="MVS_microsite" property="tipomenu" value="1">
								<div id="marcLateral">
								<h2 class="invisible">Menú general</h2>
									<jsp:include page="/v1/general/menu.jsp"/>
								</div>					
							</logic:equal>
							<logic:equal name="MVS_microsite" property="tipomenu" value="2">
								<div id="marcLateralAmbIcones">
								<h2 class="invisible">Menú general</h2>
									<jsp:include page="/v1/general/menu.jsp"/>
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
							
									
							
									<logic:present name="MVS_encuesta">
							
									<!-- titol -->
									<h2 id="titolPagina"><bean:write name="MVS_encuesta" property="traduce.titulo" /></h2>
									<!-- informacio -->

									<br/>
									<logic:equal name="MVS_encuesta" property="visible" value="S">
										<logic:notEqual name="MVS_encuesta" property="mostrar" value="S">
											<bean:message key="encuesta.gracias"/>
										</logic:notEqual>
										<logic:equal name="MVS_encuesta" property="mostrar" value="S">
											<logic:iterate name="MVS_encuesta" property="preguntas" id="i">
											<logic:equal name="i" property="visible" value="S">
												<div id="enquestaResultats" class="enquestaResultats">
												<!--  es visible -->
													<h3><bean:write name="i" property="traduce.titulo" ignore="true"/></h3>
													<logic:present name="i" property="imagen">
														<p><img src="archivopub.do?ctrl=MCRST<bean:write name="MVS_idsite" />ZI<bean:write name="i" property="imagen.id" />&amp;id=<bean:write name="i" property="imagen.id" /> " alt="" /></p>
													</logic:present>		
													<bean:define id="total" value="0" />
													<logic:notEmpty name="i" property="nrespuestas" >	
														<bean:define id="total" ><bean:write name="i" property="nrespuestas"/></bean:define>
													</logic:notEmpty>
													<% total = (total.equals("0"))?"1":total; %>
														<ul>
															<logic:iterate name="i" id="j" property="respuestas"  indexId="indice">
																<bean:define id="numrespuestas" value="0" />
																<logic:notEmpty name="j" property="nrespuestas" >	
																	<bean:define id="numrespuestas" ><bean:write name="j" property="nrespuestas"/></bean:define>
																</logic:notEmpty>
																<li><bean:write name="j" property="traduce.titulo" ignore="true" /> <em>(<%= Integer.parseInt(""+numrespuestas)*100/Integer.parseInt(""+total) %> % - <bean:write name="j" property="nrespuestas" ignore="true" /> <bean:message key="encuesta.respuestas"/>)</em>
																	<span class="barra" style="width:<%= Integer.parseInt(""+numrespuestas)*100/Integer.parseInt(""+total) %>%;">&nbsp;</span>
																</li>
															</logic:iterate>
														</ul>
														  
														<p class="votsTotals"><bean:message key="encuesta.respuestas.totales"/>: <strong><bean:write name="i" property="nrespuestas" ignore="true" /></strong></p>
												</div>		
											</logic:equal>	
											</logic:iterate>
										</logic:equal>										 
									</logic:equal>

									
									</logic:present>
														
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
