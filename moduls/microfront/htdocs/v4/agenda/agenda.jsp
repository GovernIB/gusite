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
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>" />
	<logic:present name="MVS_micrositetitulo">
		<logic:present name="MVS_agenda_diaevento">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:message key="agenda.eventos"/> <bean:write name="MVS_agenda_diaevento" format="dd/MM/yyyy"/></title>
		</logic:present>
	</logic:present>
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
							
									
									
			 <!-- informacio -->
									<div id="infoNoticia">
										<logic:present name="MVS_agenda_lista">
											<logic:notEmpty name="MVS_agenda_lista">
											

											<h2 id="titolPagina"><bean:message key="agenda.evento"/></h2>
														<bean:define id="actividadold">
														 ""
														</bean:define>
												<logic:iterate name="MVS_agenda_lista" id="i">
												
												<logic:present name="i" property="actividad.traduce">
												
												<div class="activitat">
													
														<bean:define id="actividad">
															<bean:write name="i" property="actividad.traduce.nombre" filter="false"/>
														</bean:define>
														 
														<logic:notEmpty name="i" property="actividad">
															<logic:notEqual name="actividad" value="<%=actividadold%>">			
																<p id="fontNoticia"><bean:write name="i" property="actividad.traduce.nombre" filter="false"/></p>
															</logic:notEqual>	
														</logic:notEmpty>
														
														
														
														<h3 id="titolNoticia"><bean:write name="i" property="traduce.titulo" filter="false"/></h3>
														<p id="subtitol">
														 <bean:write name="i" property="finicio" format="dd/MM/yyyy"/>
														 <bean:define id="horai">
														 	<bean:write name="i" property="finicio" format="HH:mm"/>
														 </bean:define>
														 <logic:notEqual name="horai" value="00:00">
														<bean:message key="agenda.cuando"/> <bean:write name="i" property="finicio" format="HH:mm"/>
														</logic:notEqual>
														 <logic:notEmpty name="i" property="ffin">
														  - <bean:write name="i" property="ffin" format="dd/MM/yyyy"/>
														 <bean:define id="horaf">
														 	<bean:write name="i" property="ffin" format="HH:mm"/>
														 </bean:define>
														 <logic:notEqual name="horaf" value="00:00">
														 <bean:message key="agenda.cuando"/><bean:write name="i" property="ffin" format="HH:mm"/>
														</logic:notEqual>
														</logic:notEmpty>
														 </p>
														 
														 
														<logic:notEmpty name="i" property="traduce.imagen">
															<img id="imgNoticia" src="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="i" property="id" />ZI<bean:write name="i" property="traduce.imagen.id" />&amp;id=<bean:write name="i" property="traduce.imagen.id" />" alt="" />							
														</logic:notEmpty>
														<p><bean:write name="i" property="traduce.descripcion" filter="false"/></p>
														<logic:notEmpty name="i" property="traduce.documento">
															<p><bean:message key="agenda.descdocumento"/></p>
															<p><bean:message key="general.archivo"/> <bean:write name="i" property="traduce.documento.mime" />, <bean:write name="i" property="traduce.documento.peso" /> bytes - 
															<a href="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="i" property="id" />ZI<bean:write name="i" property="traduce.documento.id" />&amp;id=<bean:write name="i" property="traduce.documento.id" />" target="blank"><bean:write name="i" property="traduce.documento.nombre" /></a></p>
														</logic:notEmpty>
														<logic:notEmpty name="i" property="traduce.url">
															<bean:define id="externa" name="i" property="traduce.url" type="java.lang.String" />
 														    <bean:define id="sitio" name="MVS_idsite"  />
														    <p><a href="<bean:write name="i" property="traduce.url" />"<%if(externa.indexOf("idsite="+sitio)==-1){out.println("target='_blanck'");}else{out.println("target='_self'");}%>>
															 <logic:notEmpty name="i" property="traduce.urlnom" >
																<bean:write name="i" property="traduce.urlnom" ignore="true"/>
															 </logic:notEmpty>
															 <logic:empty name="i" property="traduce.urlnom" >
																<bean:message key="url.adicional"/>
															 </logic:empty>													
														    </a></p>
														</logic:notEmpty>	
												</div>									
													<bean:define id="actividadold">
														<bean:write name="i" property="actividad.traduce.nombre" filter="false"/>
													</bean:define>
													
												</logic:present>
													
												</logic:iterate>
											</logic:notEmpty>	
											<logic:empty name="MVS_agenda_lista">
												<p id="fontNoticia"><bean:message key="agenda.nohay"/></p>
											</logic:empty>
										</logic:present>
									</div>
								
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
