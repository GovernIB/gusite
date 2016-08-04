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
	<logic:present name="MVS_micrositetitulo">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - Home</title>
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
			<!-- fin capçal -->
			
			<!-- continguts -->
			<div id="continguts">
				<!-- trazabilidad -->
				<ul id="mollaPa">
					<li><bean:message key="general.inicio"/></li>
				</ul>
				<!-- fin trazabilidad -->

				<!-- campanya -->
				<logic:present name="MVS_home_campanya">
					<bean:write name="MVS_home_campanya" filter="false"/>
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

					
							<table>
							<tr>
								<td width="50%"  valign="top">
									<logic:present name="MVS_home_agenda_calendario">
										<logic:notEqual name="MVS_home_agenda_calendario" value="-1">
											<br/><br/>
											<div id="agenda">
												<h2><bean:message key="agenda.agenda"/></h2>
											    <bean:write name="MVS_home_agenda_listado" filter="false"/>
											    <bean:write name="MVS_home_agenda_calendario" filter="false"/>
											</div>
										</logic:notEqual>
									</logic:present>
								</td>
								<td valign="top">
									<logic:present name="MVS_home_noticias">
										<logic:notEqual name="MVS_home_noticias" value="-1">
										    <bean:write name="MVS_home_noticias" filter="false"/>
										</logic:notEqual>
									</logic:present>
								</td>
							</tr>
							</table>
				</div>	
				<!-- fin informacio -->
				
			</div>
			<!-- fin continguts -->
			
			
			<!-- peu -->
			<jsp:include page="/v1/general/pie.jsp"/>
			<!-- fin peu -->
			
		</div>	
		<!-- fin contenedor -->
	</body>
</html>
