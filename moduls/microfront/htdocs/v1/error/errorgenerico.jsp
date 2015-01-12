<%@ page language="java"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<logic:present name="org.apache.struts.action.EXCEPTION">
<bean:define id="exception" name="org.apache.struts.action.EXCEPTION" type="java.lang.Throwable"/>
</logic:present>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">
	<title>Error</title>
	<link href="v1/css/estils.css" rel="stylesheet" type="text/css" />		
	<script type="text/javascript" src="v1/js/globales.js"></script>
	</head>
	
	<body>
		    <!-- contenidor -->
		    <div id="contenedor">
			<!-- capçal -->
				<bean:define id="tipocabecera" value="1" />
				<logic:empty name="MVS_microsite" property="tipocabecera" ><bean:define id="tipocabecera" value="1" /></logic:empty>
				<logic:equal name="MVS_microsite" property="tipocabecera" value="1"><bean:define id="tipocabecera" value="1" /></logic:equal>
				<logic:equal name="MVS_microsite" property="tipocabecera" value="0"><bean:define id="tipocabecera" value="0" /></logic:equal>
				<logic:equal name="MVS_microsite" property="tipocabecera" value="2"><bean:define id="tipocabecera" value="2" /></logic:equal>
				
				<logic:equal name="tipocabecera" value="1">
						<div id="capsal">
							<logic:equal name="MVS_microsite" property="restringido" value="S">
								<a href="http://www.caib.es/" class="destacat">
									<img class="logo" src="v1/intranet/imgs/capsal/logo.gif" alt="Logo del Govern de les Illes Balears" />
								</a>
							</logic:equal>
							<logic:notEqual name="MVS_microsite" property="restringido" value="S">
								<a href="http://www.caib.es/" class="destacat">
									<img class="logo" src="v1/imgs/cap/logo.gif" alt="Logo del Govern de les Illes Balears" />
								</a>
							</logic:notEqual>
						</div>
				</logic:equal>		

				<logic:equal name="tipocabecera" value="2">	
					<bean:write name="MVS_microsite" property="traduce.cabecerapersonal" filter="false" />
				</logic:equal>
			<!--  Fi capçal -->
				<!-- continguts -->
				<div id="continguts">
					<!-- missatge -->				
					<div id="missatge">
							<strong>
							<bean:message key="error.titulo"/>
								<logic:present name="exception">
								    <bean:write name="exception" />
							        <bean:write name="exception" property="message"/>
							    </logic:present>
							    <logic:notPresent name="exception">
									<logic:present name="MVS_errparam">
										<h2 id="titolPagina"><bean:write name="MVS_errparam" property="aviso" filter="false"/></h2>
										    <bean:write name="MVS_errparam" property="mensaje" filter="true"/>
										    <bean:write name="MVS_errparam" property="descripcion" filter="false"/>
										    <bean:write name="MVS_errparam" property="accion" filter="false"/>
								   	</logic:present>
							    </logic:notPresent>
							</strong>
					</div>	
					<!-- fi missatge -->
				</div>	
			<logic:notPresent name="MVS_microsite">
						<div id="info">
						</div>			
			</logic:notPresent>
			<!-- fi continguts -->
			</div>
			<!-- fi contenidor -->
			
</body>			
</html>