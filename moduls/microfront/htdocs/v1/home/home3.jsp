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
	</head>

	
	<body>
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v1/general/cabecera.jsp"/>

			<div id="continguts">
					<!-- trazabilidad -->
					<ul id="mollaPa">
						<li><bean:message key="general.inicio"/></li>
					</ul>
					<table cellpadding="0" cellspacing="0" id="laTablaObligada">
					<tr>
						<logic:present name="MVS_microsite">
							<logic:equal name="MVS_microsite" property="tipomenu" value="1">
								<td id="marcLateral">
									<jsp:include page="/v1/general/menu.jsp"/>
								</td>
							</logic:equal>
							<logic:equal name="MVS_microsite" property="tipomenu" value="2">
								<td id="marcLateralAmbIcones" valign="top">
									<jsp:include page="/v1/general/menu.jsp"/>
								</td>
							</logic:equal>
							<logic:notEqual name="MVS_microsite" property="tipomenu" value="0">
								<td>&nbsp;</td>
							</logic:notEqual>
						</logic:present>	
						<logic:notPresent name="MVS_microsite">
							<td>&nbsp;</td>
						</logic:notPresent>
						<td id="info">
							<p style="text-align:center;">
								<logic:notEmpty name="MVS_microsite" property="imagenPrincipal">
										<img src="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="MVS_microsite" property="id" />ZI<bean:write name="MVS_microsite" property="imagenPrincipal.id" />&amp;id=<bean:write name="MVS_microsite" property="imagenPrincipal.id" />" alt="" />
								</logic:notEmpty>
							</p>
						</td>
					</tr>
					</table>
			</div>
				

			<!-- peu -->
			<jsp:include page="/v1/general/pie.jsp"/>
		</div>	
	</body>
</html>
