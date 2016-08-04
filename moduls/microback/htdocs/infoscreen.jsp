<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Microsites - Govern de les Illes Balears</title>

    <script type="text/javascript" src="js/subMenus.js"></script>
	<!-- estils -->
	<link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
	<!-- /estils -->
	
	<!--[if lt IE 7]>
		<link rel="stylesheet" type="text/css" href="css/estils_ie6.css" media="screen" />
	<![endif]-->

</head>

<body>
	
     
	<!-- contenidor -->
	<div id="contenidor">
	
		<!-- cap -->
		<jsp:include page="cabecera.jsp"/>
		<!-- /cap -->
		
		<!-- marc lateral -->
		<jsp:include page="menuLateralIzq.jsp"/>
		<!-- /marc lateral -->
		
		<!-- continguts -->
		<div id="continguts">
			
			<h1><bean:message key="menu.mensaje" /></h1>
			
			
			<!-- text info -->
			<div id="textInfo">
				<html:messages id="message" message="true">
				<%= message %><br/>
				</html:messages>
				<br/>	
				<logic:present name="SVS_otrainfo">
					<p><pre><bean:write name="SVS_otrainfo"/></pre></p>
				</logic:present>
			</div>
			<!-- /text info -->
		
		</div>
		<!-- /continguts -->
		
	</div>
	<!-- contenidor -->

</body>

</html>