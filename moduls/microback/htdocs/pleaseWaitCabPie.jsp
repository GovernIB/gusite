<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
</head>

<body>	
	
	<!-- Missatge de pagina -->
	
	  <table cellpadding="10">
	        <tr>
	           <td align="left"><bean:message key="pleasewait.peticion" />...</td>     
	        </tr>
	        <tr>
	        	<td align="left"><bean:message key="pleasewait.favor" /></td>
	        </tr>
	  </table>

<script type="text/javascript">
<!--
	window.parent.location.href="cabeceraPieEdita.do?accion=cabpie&refresco=si&idsite=<bean:write name='MVS_microsite' property='id' />";
// -->
</script>	

</body>
</html>

