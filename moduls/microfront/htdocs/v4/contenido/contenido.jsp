<%@ page language="java"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>" />
	<logic:present name="MVS_contenido" property="traduce">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_contenido" property="traduce.titulo" filter="false"/></title>
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
			<!-- /capçal -->

			
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
				&nbsp;
				<logic:equal name="MVS_microsite" property="tipomenu" value="0">
					<div id="infoNoMenu">
				</logic:equal>
				<logic:notEqual name="MVS_microsite" property="tipomenu" value="0">
					<div id="info">
				</logic:notEqual>								

					
						<logic:present name="MVS_contenido" property="traduce">
							<logic:equal name="MVS_tipobeta" value="beta">
								<bean:write name="MVS_contenido" property="traduce.txbeta" filter="false"/>						
							</logic:equal>
							<logic:notEqual name="MVS_tipobeta" value="beta">
								<bean:write name="MVS_contenido" property="traduce.texto" filter="false"/>
							</logic:notEqual>
						</logic:present>
						
				</div>	
				<!-- fin informacio -->
			</div>
			<!-- fin continguts -->
			
			
			<!-- peu -->
			<jsp:include page="/v4/general/pie.jsp"/>
			<!-- fin peu -->
			
		</div>	
		<!-- fin contenedor -->
	</body>
</html>



