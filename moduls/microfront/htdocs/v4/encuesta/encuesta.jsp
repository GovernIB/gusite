<%@ page language="java"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">	
	<logic:present name="MVS_encuesta">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_encuesta" property="traduce.titulo" /></title>
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
				<logic:present name="MVS_manteniment">
				<div align="center" style="border:2px solid; border-radius:5px; padding:10px;">
					<bean:message key="encuesta.mantenimiento"/>
				</div>
				</logic:present>					
				<logic:notPresent name="MVS_manteniment">
					<%=request.getAttribute("MVS_htmlEncuestaParsed").toString()%>
				</logic:notPresent>		
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
