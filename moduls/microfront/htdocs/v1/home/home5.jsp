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
			
			<!-- continguts -->
			<div id="continguts">
				<!-- trazabilidad -->
				<ul id="mollaPa">
					<li><bean:message key="general.inicio"/></li>
				</ul>

				<logic:present name="MVS_home_campanya">
					<bean:write name="MVS_home_campanya" filter="false"/>
				</logic:present>

				<!-- Capa separadora. NO quitar nunca  -->	
				<div id="enllasDestPeu"></div>
				<table id="contingutsDesplegats" cellpadding="0" cellspacing="10">
				<tr>
					<logic:iterate name="MVS_home_listatotal" id="i" indexId="indice">
					<td>
						<h2><bean:write name="i" property="txmenu"/></h2>
							<ul>
							<logic:iterate name="i" id="j" property="listapaginas">
								<li><a href="<bean:write name="j" property="key"/>"><bean:write name="j" property="value"/></a></li>
							</logic:iterate>
							</ul>
					</td>
					<%=((indice.intValue()%2==1) ? "</tr><tr>" : "")%>
					</logic:iterate>
				</tr>
				</table>				
				
			</div>

			<!-- peu -->
			<jsp:include page="/v1/general/pie.jsp"/>
		</div>	
	</body>
</html>
