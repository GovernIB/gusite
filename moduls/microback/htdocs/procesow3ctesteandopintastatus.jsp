<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta http-equiv="cache-control" content="no-cache"/>  <!-- For HTTP 1.1 -->
	<meta http-equiv="Pragma" content="no-cache"/>         <!-- For HTTP 1.0 -->
	<logic:notEqual name="MVS_valorbarraestado" value="100">
		<meta http-equiv="refresh" content="1; URL=procesow3ctesteandopintastatus.do?idstatusbar=<bean:write name="MVS_idbarraestado"/>"/>
	</logic:notEqual>
	<title>Gestor Microsites - StatusBar</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<style type="text/css">
			<!--
				div#porcentaje { float:right; width:4em; height:1.2em; font-weight:bold; color:#c00; text-align:right; }
				div#contenedor { position:relative; width:25em; height:1.2em; border:1px solid #c00; overflow:hidden; }
				span#barra { display:block; overflow:hidden; position:absolute; z-index:2; top:0; left:0; width:0%; height:1.2em; background-color:#c00; }
			-->
	</style>
</head>


<body>

	<div id="info">
		<div id="porcentaje">
			<bean:write name="MVS_valorbarraestado"/> % 
		</div>
		<div id="contenedor">
			<span id="barra" style="width:<bean:write name="MVS_valorbarraestado"/>%"></span>
		</div>
		
		<logic:equal name="MVS_valorbarraestado" value="100">
		<br/>
		<p>
		<a href="procesow3c.do?idsite=<bean:write name="MVS_microsite" property="id"/>" target="_parent">Ver resultados Test Accessibilitat W3C</a>
		</p>
		</logic:equal>
	</div>


</body>

</html>