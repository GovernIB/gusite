<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<logic:present name="org.apache.struts.action.EXCEPTION">
<bean:define id="exception" name="org.apache.struts.action.EXCEPTION" type="java.lang.Throwable"/>
</logic:present>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>" />
	<title>
		<bean:message key="msg.titulo"/>
	</title>
	<link href="v4/css/estils.css" rel="stylesheet" type="text/css" />	
	<script type="text/javascript" src="v4/js/globales.js"></script>
		<jsp:include page="/v4/general/scriptmenu.jsp"/>
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
								<a href="/root/index.do" id="lang" name="lang" accesskey="0" class="destacat">
									<img class="logo" src="v4/intranet/imgs/capsal/logo.gif" alt="Logo del Govern de les Illes Balears" />
								</a>
							</logic:equal>
							<logic:notEqual name="MVS_microsite" property="restringido" value="S">
								<a href="/root/index.do" id="lang" name="lang" accesskey="0" class="destacat">
									<img class="logo" src="v4/imgs/cap/logo.gif" alt="Logo del Govern de les Illes Balears" />
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
							<bean:message key="msg.titulo"/>
								<logic:present name="msg">									
								    <bean:write name="msg" filter="false"/>								    
							   	</logic:present>
							</strong>
					</div>	
					<!-- fi missatge -->
				</div>	
			<logic:notPresent name="msg">
				<div id="info">
				</div>			
			</logic:notPresent>
			<!-- fi continguts -->
			</div>
			<!-- fi contenidor -->
			
</body>			
</html>